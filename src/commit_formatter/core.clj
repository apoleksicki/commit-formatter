(ns commit-formatter.core
  (:use clojure.test [clojure.string :only (split)]))


;(defn format1 [s1 s2]
;  (if (= s2 "\n" ) 
;    (apply str s1 s2)
;    (if (>= ))))
;

(defn length-of-line [line]
  (let [line-start (.lastIndexOf line "\n")]
    (if (= -1 line-start)
      (count line)
      (- (count line) line-start))))


(defn add-word [s1 s2]
  (let []))

(defn format-message [message]
  (reduce (fn [s1 s2]
            (if (cou)))(split message #" ")))

(defn test-format []
  (is (= "Ta linia jest zbyt długa i nie pasuje do uzgodnionego format. Dlatego\nteż, należy ją podzielić na dwie."
         (format-message "Ta linia jest zbyt długa i nie pasuje do uzgodnionego format. Dlatego też, należy ją podzielić na dwie."))))