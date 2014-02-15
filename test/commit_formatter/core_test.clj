(ns commit-formatter.core
  (use clojure.test))

(def test-line "Ta linia jest zbyt długa i nie pasuje do uzgodnionego format. Dlatego też, należy ją podzielić na dwie.")

(def expected-test-line "Ta linia jest zbyt długa i nie pasuje do uzgodnionego format. Dlatego\nteż, należy ją podzielić na dwie.")

(def test-url "https://www.google.dk/search?q=take+clojure&oq=take+clojure&aqs=chrome..69i57j0l5.1546j0j7&sourceid=chrome&espv=210&es_sm=93&ie=UTF-8#es_sm=93&espv=210&q=long+url")

(def weird-line "aaaaaaaaaaaaaaaaaaaaaaaaaaa bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb hhhhh")

(def expected-weird-line (format-message "aaaaaaaaaaaaaaaaaaaaaaaaaaa bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb\nhhhhh"))

(deftest test-format []
         (is (= expected-test-line (format-message test-line)))
         (is (= expected-test-line (format-message expected-test-line)))
         (is (= test-url (format-message test-url)))
         (is (= expected-weird-line (format-message weird-line))))