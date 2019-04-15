/*
 * Copyright (c) 2014 Weidong Fang
 */

#include "tinyhash.h"

#include <assert.h>
#include <ctype.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h>

typedef uint32_t HASH;

typedef struct Slot {
    const void  *key;
    const void  *value;
    struct Slot *next;
    HASH         hash;
    uint8_t      taken;
} Slot;

typedef HASH    (*Hasher) (const void *);
typedef int     (*Tester) (const void *, const void *);

struct TinyHash {
    Slot *slot;
    Slot *free_slot;

    uint32_t size;
    uint32_t count;

    float    max_full;
    uint32_t max_count;

    Hasher hasher;
    Tester tester;
};

#define TINY_OK         0
#define TINY_ERROR     -1
#define TINY_SIZE       64          /* default size */
#define TINY_MAX        1073741824  /* maximum size */
#define HOME_OF(t,h)    (&(t)->slot[(h) % (t)->size])

#define xmalloc         malloc
#define xfree           free

int tiny_hash_resize(TinyHash *, uint32_t);

static Slot *tiny_hash_locate(TinyHash *, HASH, const void *);
static int tiny_hash_test(TinyHash *, Slot *, HASH, const void *);

static HASH tiny_hash_string_hasher(const void *key);
static int  tiny_hash_string_tester(const void *s1, const void *s2);

static HASH tiny_hash_string_hasher_caseless(const void *key);
static int  tiny_hash_string_tester_caseless(const void *s1, const void *s2);

uint32_t
tiny_hash_count(TinyHash *t)
{
    return t->count;
}

TinyHash *
tiny_hash_create(uint32_t size, Hasher hasher, Tester tester, float max_full)
{
    TinyHash *t;

    t = (TinyHash *) xmalloc (sizeof (TinyHash));

    if (t == NULL) {
        /* out of memory */
        return NULL;
    }

    memset(t, 0, sizeof(*t));

    t->max_full = max_full > 0.0 ? max_full : 1.0;

    if (tiny_hash_resize(t, size) != TINY_OK) {
        xfree(t);
        return NULL;
    }

    t->hasher = hasher ? hasher : tiny_hash_string_hasher;
    t->tester = tester ? tester : tiny_hash_string_tester;

    return t;
}

TinyHash *
tiny_hash_create_simple(uint32_t size, int caseless)
{
    if (caseless) {
        return tiny_hash_create(size, tiny_hash_string_hasher_caseless,
                tiny_hash_string_tester_caseless, 0.75);
    }
    else {
        return tiny_hash_create(size, tiny_hash_string_hasher,
                tiny_hash_string_tester, 0.75);
    }
}

void
tiny_hash_destroy(TinyHash *t)
{
    xfree(t->slot);
    xfree(t);
}

void
tiny_hash_clear(TinyHash *t)
{
    if (t->count > 0) {
        memset(t->slot, 0, t->size * sizeof(Slot));
        t->free_slot = t->slot + t->size;
        t->count = 0;
    }
}

int
tiny_hash_exists(TinyHash *t, const void *key)
{
    HASH  h = t->hasher(key);
    Slot *n = tiny_hash_locate(t, h, key);
    return n ? 1 : 0;
}

static Slot *
tiny_hash_locate(TinyHash *t, HASH  h, const void *k)
{
    Slot *m = HOME_OF(t, h);

    do {
        if (tiny_hash_test(t, m, h, k)) {
            return m;
        }
        else {
            m = m->next;
        }
    } while (m);

    return NULL;
}

void *tiny_hash_get(TinyHash *t, const void *key) {
    HASH  h = t->hasher(key);
    Slot *n = tiny_hash_locate(t, h, key);
    return n ? (void *) n->value : NULL;
}

static int
tiny_hash_insert(TinyHash *t, HASH h, const void *key, const void *value)
{
    Slot *m;

    if (t->count >= t->max_count) {
        if (tiny_hash_resize(t, 2 * t->size) != TINY_OK) {
            return TINY_ERROR;
        }
    }

    m = HOME_OF(t, h);

    if (m->taken) {
        Slot *n  = NULL;
        Slot *mm = HOME_OF(t, m->hash);

        for (n = NULL; t->free_slot > t->slot;) {
            n = --t->free_slot;
            if (!n->taken) {
                break;
            }
        }

        assert(n && !n->taken);

        if (mm == m) {
            n->next = m->next;
            m->next = n;
            n->taken = 1;
            m = n;
        } else {
            while (mm->next != m) {
                mm = mm->next;
            }
            mm->next = n;
            *n = *m;
            m->next = NULL;
        }
    }
    else {
        m->next  = NULL;
        m->taken = 1;
    }

    m->key   = key;
    m->value = value;
    m->hash  = h;

    t->count++;

    return TINY_OK;
}

static void
tiny_hash_reclaim(TinyHash *t, Slot *n)
{
    memset(n, 0, sizeof(*n));
    if (n >= t->free_slot) {
        t->free_slot = n + 1;
    }
}

int
tiny_hash_remove(TinyHash *t, const void *k)
{
    HASH  h = t->hasher(k);
    Slot *m = HOME_OF(t, h);    /* tortoise */
    Slot *n = m;                /* hare */

    do {                        /* tiny_hash_locate */
        if (tiny_hash_test(t, n, h, k)) {
            break;
        }
        else {
            m = n;
            n = n->next;
        }
    } while (n);

    if (!n) {
        return TINY_ERROR;
    }

    if (m == n) {
        if (m->next) {
            Slot *p = m->next;
            *m = *m->next;
            tiny_hash_reclaim(t, p);
        }
        else {
            tiny_hash_reclaim(t, n);
        }
    }
    else {
        m->next = n->next;
        tiny_hash_reclaim(t, n);
    }

    assert(t->count > 0);

    t->count--;

    return TINY_OK;
}

int
tiny_hash_resize(TinyHash *t, uint32_t size)
{
    uint32_t old_size;
    Slot    *old_slot;
    uint32_t i;

    if (size > TINY_MAX) {
        /* too big */
        return TINY_ERROR;
    }

    if (size == 0) {
        size = TINY_SIZE;
    }

    old_size = t->size;
    old_slot = t->slot;

    t->slot = (Slot *) xmalloc(size * sizeof(Slot));

    if (t->slot == NULL) {
        /* out of memory */
        t->slot = old_slot;
        return -1;
    }

    memset(t->slot, 0, size * sizeof(Slot));

    t->size = size;
    t->free_slot = t->slot + size;
    t->count = 0;
    t->max_count = size * t->max_full;

    for (i = 0; i < old_size; i++) {
        Slot *slot = old_slot + i;
        if (slot->taken) {
            tiny_hash_insert(t, slot->hash, slot->key, slot->value);
        }
    }

    xfree(old_slot);

    return TINY_OK;
}

int
tiny_hash_put(TinyHash *t, const void *key, const void *value) {
    HASH  h = t->hasher(key);
    Slot *n = tiny_hash_locate(t, h, key);

    if (n) {
        n->value = value;
        return TINY_OK;
    }

    return tiny_hash_insert(t, h, key, value);
}

static int
tiny_hash_test(TinyHash *t, Slot *slot, HASH hash, const void *key)
{
    if (slot->hash != hash) {
        return 0;
    }
    return t->tester(slot->key, key);
}

static int tiny_hash_string_tester(const void *s1, const void *s2) {
    return !strcmp((const char *) s1, (const char *) s2);
}

static int tiny_hash_string_tester_caseless(const void *s1, const void *s2) {
    return !strcasecmp((const char *) s1, (const char *) s2);
}

/**
 * 32-bit FNV1-1a algorithm
 */
static HASH tiny_hash_string_hasher(const void *key) {
    unsigned char *s = (unsigned char *) key;
    HASH h = 2166136261;
    while (*s) {
        h ^= (u_int32_t) *s++;
        h *= (HASH) 0x01000193;
    }
    return h;
}

/**
 * 32-bit FNV1-1a algorithm (caseless)
 */
static HASH tiny_hash_string_hasher_caseless(const void *key) {
    unsigned char *s = (unsigned char *) key;
    HASH h = 2166136261;
    while (*s) {
        char c;
        c = tolower(*s++);
        h ^= (u_int32_t) c;
        h *= (HASH) 0x01000193;
    }
    return h;
}

const TinyHashIterator *tiny_hash_first(TinyHash *t) {
    Slot *slot = t->slot;
    while (slot < t->slot + t->size && !slot->taken) {
        slot++;
    }
    if (slot < t->slot + t->size && slot->taken) {
        return (const TinyHashIterator *) slot;
    }
    return NULL;
}

const TinyHashIterator *tiny_hash_next(TinyHash *t, const TinyHashIterator *p) {
    Slot *slot = (Slot *) p;

    ++slot;

    while (slot < t->slot + t->size && !slot->taken) {
        slot++;
    }

    if (slot < t->slot + t->size && slot->taken) {
        return (const TinyHashIterator *) slot;
    }

    return NULL;
}

#ifdef TEST
#include <stdio.h>
void tiny_hash_dump(TinyHash *t, char *buf, int length) {
    uint32_t i, so;
    *buf = '\0';
    for (i = 0, so = 0; i < t->size; i++) {
        Slot *slot = &t->slot[i];
        if (slot->taken) {
            so += snprintf(buf + so, length - so, "(%d,%d,%d)", i,
                    slot->hash, slot->next ? (int)(slot->next - t->slot) : -1);
        }
    }
}
#endif
