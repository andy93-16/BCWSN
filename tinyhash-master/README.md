tinyhash
========
A simple, fast hash table implemented in C.

Features
--------

- Whenever it is possible, entries are put in their 'home' positions when they 
are inserted into the table. Entries with same home positions are chained
together.
- Hash values of the keys are cached in the table. When an equality test is 
needed, the hash values will be compared first before calling a test function. 
This makes tiny hash very retrieval friendly as keys' equality tests are usually
much more expensive than those of the hash values (compare the costs of 
`strcmp("http://example.com/node/1", "http://example.com/node/2")` and 
`0x7f1b33fa == 0x62d9a0e3`).
- Light weight (~300 lines of code) and well tested (see `test.c` for test 
cases).


Usage
-----

```c
    /* Create a simple string hash table */
    TinyHash *tiny = tiny_hash_create_simple(0);

    /* Put a key/value pair into the table */
    tiny_hash_put(tiny, "Hello", "World!");

    /* Test if a key exists */
    assert(tiny_hash_exists(tiny, "Hello"));

    /* Retrieve the value associated with a key */
    printf("%s\n", (const char *)tiny_hash_get(tiny, "Hello"));

    /* Remove an entry */
    tiny_hash_remove(tiny, "Hello");

    /* Get the number of entries in the table */
    assert(tiny_hash_count(tiny) == 0);

    /* Free the spaces */
    tiny_hash_destroy(tiny);
```

Todo
----

- Entry iteration
- More memory efficiency. On a X86 64 machine, each slot in the table takes 32
bytes. This can be reduced to 24 by changing the `next` pointer to a uint32 
offset and using a special, invalid key pointer (e.g. `NULL` or `(void *)-1`) to 
indicate if a node is taken.
- Further performance improvements can be made by 1) using a `prev` field in 
the `Slot` so that removing an element does need to start from the head of a
chain, and 2) returning the last element found in a chain, if there is one, from 
`tiny_hash_locate()` so that `tiny_hash_put()` element can be made quicker.

Others
------

If you find any bugs, or you have done any benchmarking to the code and you
would like to let me know, please write to wdfang@gmail.com.
