(defproject commit-formatter "1.0.0-SNAPSHOT"
  :description "FIXME: write"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [snipsnap "0.1.0"]
                 [org.clojure/math.combinatorics "0.0.3"]]
  :plugins [[lein2-eclipse "2.0.0"]]
  :aot [commit-formatter.ui]
  :main commit-formatter.ui
  :test-selectors {:default (complement :integration)
                   :integration :integration
                   :all (fn [_] true)})