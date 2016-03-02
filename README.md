# clj-trie

A Clojure library designed to implement [tries](https://en.wikipedia.org/wiki/Trie).
A trie is an ordered tree data structure that is used to store a dynamic set or associative array where the keys are usually strings.

## Usage

Tries are interesting datastructures. Here is a list of potential applications:

1. As a replacement of other Data Structures - Hash Table / Binary Search Tree
    * Lookups in a trie is faster in the worst case, O(m) time, compared to an imperfect hash table
    * No collisions of different keys
    * No hash function needed
    * Can provide alphabetical ordering of the entries by key
2. Dictionary representation

A common application is storing a set of words in the trie. A trie is then able to quickly search for, insert and delete entries. Tries are often use in spell checking or autocompletion systems.

## Get Started

### Inserts
```
(trie ["doo" "foo" "doa" "foot"])
(into (trie) ["doo" "foo" "doa" "foot"])
(conj (trie) "doo")
```

### Lookups
```
(let [t (trie ["doo" "foo" "doa" "foot"])]
  (get t "doo") ; => "doo"
  (get t "boo") ; => nil
)
```

### Autocompletion
```
(let [t (trie ["doo" "foo" "doa" "foot"])]
  (t "do")     ; => ["doa", "doo"]
  (t "boo")    ; => nil
  (t "f")      ; => ["foo", "foot"]
)

```
## Tests

Property testing is used for testing the trie implementation.
To run the tests:

```
lein repl
(in-ns 'trie.core)
(use 'clojure.test)
(run-tests)
```

## License

Copyright © 2016 Arthur Caillau

Distributed under The MIT License (MIT)
