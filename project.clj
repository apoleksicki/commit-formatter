(defproject commit-formatter "0.1.0-SNAPSHOT"
  :description "FIXME: write"
  :dependencies [[org.clojure/clojure "1.5.0"]
                 [snipsnap "0.1.0" :exclusions[[org.clojure/clojure]]]
                 [org.clojure/core.match "0.2.1"]
                 [seesaw "1.4.4"]]
  :plugins [[lein2-eclipse "2.0.0"]]
  :aot [commit-formatter.ui]
  :main commit-formatter.ui
  :test-selectors {:default (complement :integration)
                   :integration :integration
                   :all (fn [_] true)})