/*
 * Copyright (c) 2014 Weidong Fang
 */

#ifndef __TINY_HASH_H__
#define __TINY_HASH_H__

#ifdef __cplusplus
extern "C" {
#endif

#include <stdint.h>

typedef struct TinyHash TinyHash;

TinyHash *tiny_hash_create_simple(uint32_t size, int caseless);
TinyHash *tiny_hash_create(uint32_t size, uint32_t (*hasher) (const void *),
        int (*tester) (const void *, const void *), float max_full);
void tiny_hash_clear(TinyHash *);
void tiny_hash_destroy(TinyHash *);

int tiny_hash_put(TinyHash *, const void *key, const void *value);
void *tiny_hash_get(TinyHash *, const void *key);
int tiny_hash_exists(TinyHash *, const void *key);
int tiny_hash_remove(TinyHash *, const void *key);

uint32_t tiny_hash_count(TinyHash *);

typedef struct {
    const void  *key;
    const void  *value;
} TinyHashIterator;

const TinyHashIterator *tiny_hash_first(TinyHash*);
const TinyHashIterator *tiny_hash_next(TinyHash*, const TinyHashIterator*);

#ifdef __cplusplus
}
#endif

#endif // __TINY_HASH_H__
