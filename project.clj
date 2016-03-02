(defproject trie "0.1.0"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript  "1.7.170"]
                 [org.clojure/test.check "0.9.0"]]

  :min-lein-version "2.5.3"

  :source-paths  ["src"]

  :plugins  [[lein-cljsbuild  "1.1.1"]]

  :clean-targets ^{:protect false}  ["resources/public/js/compiled"  "target"]

  :cljsbuild  {:builds  [{:id  "dev"
                          :source-paths  ["src"]
                          :compiler  {:main trie.core
                                      :output-to  "resources/public/js/compiled/app.js"
                                      :output-dir  "resources/public/js/compiled/out"
                                      :asset-path  "js/compiled/out"
                                      :source-map-timestamp true}}

                         {:id  "min"
                          :source-paths  ["src"]
                          :compiler  {:main trie.core
                                      :output-to  "resources/public/js/compiled/app.js"
                                      :optimizations :advanced
                                      :closure-defines  {goog.DEBUG false}
                                      :pretty-print false}}]})
