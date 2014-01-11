(ns commit-formatter.core
  (use clojure.test [clojure.string :only (join blank?)]))

(def line-length 72)

(def test-line "Ta linia jest zbyt długa i nie pasuje do uzgodnionego format. Dlatego też, należy ją podzielić na dwie.")

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

(defn calculate-next-end-of-line [line]
  (if (< (.length line) line-length)
    (- (.length line) 1)
    (.lastIndexOf (subs line 0 line-length) " ")))

(defn format-message [message]
  (defn format-intern [formatted to-format]
    (if (blank? to-format)
      formatted
      (let [eol (calculate-next-end-of-line to-format)]
;         (println eol)
         (print-message (list "to format: " to-format))
;         (println (subs to-format 0 eol))
;         (println (subs to-format  (+ eol 1) (.length to-format)))
         (let [foo (next-to-format eol to-format)]           
         (format-intern (conj formatted (subs to-format 0 
                                              (if (blank? foo) (+ eol 1) eol))) 
                        foo)))))
  (join "\n" (reverse (format-intern () message)))
;  (format-intern () message)
  )


(defn next-to-format [eol to-format] 
  (subs to-format 
        (+ eol 1) 
        (.length to-format)))

(defn print-message [messages] 
  (println (join " " messages)))

(defn test-format []
  (is (= "Ta linia jest zbyt długa i nie pasuje do uzgodnionego format. Dlatego\nteż, należy ją podzielić na dwie."
         (format-message test-line))))




;(defn format [line]
  