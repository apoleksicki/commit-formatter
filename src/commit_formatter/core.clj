(ns commit-formatter.core
  (use [clojure.string :only (join blank? split)]))

(def line-length 72)

(def header-length 52)

(defn get-next-chunk [line]
  (if (or (< (.length line) line-length) 
          (not (.contains (subs line 0 line-length) " ")))
    (if (and  (> (.length line) line-length) (.contains line " "))
      (.indexOf line " ")
      (- (.length line) 1))
    (.lastIndexOf (subs line 0 line-length) " ")))

(defn next-to-format [eol to-format] 
  (subs to-format 
        (+ eol 1) 
        (.length to-format)))

(defn format-message [message]
  (letfn [(format-intern [formatted to-format]
                         (if (blank? to-format)
                           formatted
                           (let [eol (get-next-chunk to-format)]
                             (let [new-to-format (next-to-format eol to-format)]
                               ;(println new-to-format)           
                               (format-intern (conj formatted (subs to-format 0 
                                                                    (if (blank? new-to-format) 
                                                                      (+ eol 1) 
                                                                      eol))) 
                                              new-to-format)))))]
    (join "\n"(map (fn [line] (join "\n" (reverse (format-intern () line)))) (split message #"\n")))))

(defn print-message [messages] 
  (println (join " " messages)))


  