(ns trie.core
  (:require [clojure.string :as s]))

(defn- make-empty-trie
  "Creates an empty trie with empty lexicon."
  []
  {:value ""})

(defn- trie->seq
  "Returns all the elements inserted in trie as a sequence."
  [{:keys [value children key] :as trie}]
  (loop [stack [trie]
         words []]
    (if (not (seq stack))
      (seq words)
      (let [{:keys [value children key]} (peek stack)]
        (recur (into (pop stack) (vals children))
               (if key (conj words value) words))))))

(defn- apply-trie
  [{:keys [value key children] :as trie} [c & cs :as string]]
  (cond
    (not (seq string))           (trie->seq trie)
    (not (get children (str c))) []
    :else                        (recur (get children (str c)) cs)))

(defn- conj-trie
  "Returns a trie with the string in it.
   Not TCO, may stack overflow if very long words are conjed.
   Should not be an issue in practice"
  [{:keys [value children] :as trie} [c & cs :as string]]
  (let [ch (str c)]
    (cond
      ; A leaf is reached and last letter in string
      (and (not (get children ch)) (not (seq cs)))
      (assoc-in trie [:children (str c)] {:value (str value c) :key (gensym)})

      ; A leaf is reached and still some letters
      (and (not (get children ch)) (seq cs))
      (assoc-in trie [:children ch] (conj-trie {:value (str value c)} cs))

      ; No more letters but already present in the trie
      (and (get children ch) (not (seq cs)))
      (assoc-in trie [:children ch :key] (gensym))

      ; Already in trie and still letters
      (and (get children ch) (seq cs))
      (update-in trie [:children ch] conj-trie cs))))

(defn- into-trie
  [trie coll]
  (reduce conj-trie trie coll))

#?(:clj
    (deftype Trie [trie lexicon-set]

      clojure.lang.ILookup
      (valAt [self prefix-string] (get lexicon-set prefix-string))
      (valAt [self prefix-string default] (get lexicon-set prefix-string default))

      clojure.lang.IPersistentCollection
      (seq [self]     (seq lexicon-set))
      (cons [self o]  (Trie. (conj-trie trie o) (conj lexicon-set o)))
      (empty [self]   (Trie. (make-empty-trie) #{}))
      (equiv [self o] (and (instance? Trie o) (= trie (.trie o))))

      clojure.lang.IFn
      (invoke [self prefix-string]
        (let [completions (apply-trie trie prefix-string)]
          (if (seq completions) completions nil)))

      Object
      (toString [self] (str (trie->seq trie)))))

#?(:cljs
    (deftype Trie [trie lexicon-set]

      cljs.core/ILookup
      (-lookup [self prefix-string] (get lexicon-set prefix-string))
      (-lookup [self prefix-string default] (get lexicon-set prefix-string default))

      cljs.core/ICollection
      (-conj [self o] (Trie. (conj-trie trie o) (conj lexicon-set o)))

      cljs.core/ICounted
      (-count [self] (count lexicon-set))

      cljs.core/IEmptyableCollection
      (-empty [self] (Trie. (make-empty-trie) #{}))

      cljs.core/IFn
      (-invoke [self prefix-string]
        (let [completions (apply-trie trie prefix-string)]
          (if (seq completions) completions nil)))

      Object
      (toString [self] (str (trie->seq trie)))))

(defn trie
  ([]     (Trie. (make-empty-trie) #{}))
  ([coll] (into (trie) (set (map s/lower-case coll)))))
