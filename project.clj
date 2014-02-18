(defproject commit-formatter "0.0.3"
  :description "FIXME: write"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [snipsnap "0.1.0"]
                 [org.clojure/core.match "0.2.1"]]
  :plugins [[lein2-eclipse "2.0.0"]]
  :aot [commit-formatter.ui]
  :main commit-formatter.ui
  :test-selectors {:default (complement :integration)
                   :integration :integration
                   :all (fn [_] true)})