(ns commit-formatter.core
  (use clojure.test [clojure.string :only (join blank? split)]))

(def line-length 72)

(def test-line "Ta linia jest zbyt długa i nie pasuje do uzgodnionego format. Dlatego też, należy ją podzielić na dwie.")

(def expected-test-line "Ta linia jest zbyt długa i nie pasuje do uzgodnionego format. Dlatego\nteż, należy ją podzielić na dwie.")

(defn add-word [s1 s2]
  (let []))

(defn calculate-next-end-of-line [line]
  (if (< (.length line) line-length)
    (- (.length line) 1)
    (.lastIndexOf (subs line 0 line-length) " ")))

(defn next-to-format [eol to-format] 
  (subs to-format 
        (+ eol 1) 
        (.length to-format)))

(defn format-message [message]
  (defn format-intern [formatted to-format]
    (format "to-format: %s" to-format)
    (if (blank? to-format)
      formatted
      (let [eol (calculate-next-end-of-line to-format)]
                (let [new-to-format (next-to-format eol to-format)]           
          (format-intern (conj formatted (subs to-format 0 
                                               (if (blank? new-to-format) 
                                                 (+ eol 1) 
                                                 eol))) 
                         new-to-format)))))
  (join "\n"(map (fn [line] (join "\n" (reverse (format-intern () line)))) (split message #"\n"))))

(defn print-message [messages] 
  (println (join " " messages)))

(defn test-format []
  (is (= expected-test-line (format-message test-line)))
  (is (= expected-test-line (format-message expected-test-line))))
  