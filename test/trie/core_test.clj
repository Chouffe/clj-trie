(ns trie.core-test
  (:require [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :refer [defspec]]
            [trie.core :refer :all]))

(def words-gen
  "A words generator"
  (->> gen/string-alpha-numeric
       (gen/such-that not-empty)
       gen/set
       (gen/such-that seq)))

(def insert-trie-property
  (prop/for-all [words words-gen]
    (->> words trie seq set (= words))))

(def autocomplete-property
  (prop/for-all [words words-gen]
    (let [prefix-string (first (shuffle words))]
      (= (set ((trie words) prefix-string))
         (set (filter (partial re-matches
                               (re-pattern (str "^" prefix-string ".*")))
                      words))))))

(defspec should-insert-all-in-trie 100 insert-trie-property)
(defspec should-autocomplete 100 autocomplete-property)
