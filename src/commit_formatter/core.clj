(ns commit-formatter.core
  (use [clojure.string :only (join blank? split)]
       [clojure.core.match :only (match)]))

(def line-length 72)

(def header-length 52)

(def header-message-separator "\n\n")

(defn calculate-separator-index [message]
  (.indexOf message header-message-separator))

(defn validate-message [message]
  (let [index (calculate-separator-index message)]
    (and (not (= index -1))
         (<= index header-length))))

(defn convert-message [message]
  (let [index (calculate-separator-index message)]
        {:header (subs message 0 index) :message (subs message (+ index 2))}))

(defn get-next-chunk [line]
  (let [shorter-than-max (< (.length line) line-length)
        no-spaces-in-chunk (not (.contains (if shorter-than-max line (subs line 0 line-length)) " "))
        spaces-in-line (.contains line " ")] 
    (match [shorter-than-max no-spaces-in-chunk spaces-in-line]
           [true _ _] (- (.length line) 1)
           [false true true] (.indexOf line " ")
           [false true false] (- (.length line) 1) 
           :else (.lastIndexOf (subs line 0 line-length) " "))))

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
                               (format-intern (conj formatted 
                                                    (subs to-format 0 
                                                          (if (blank? new-to-format) 
                                                            (+ eol 1) 
                                                            eol))) 
                                              new-to-format)))))]
    (join "\n"(map (fn [line] (join "\n" (reverse (format-intern () line)))) (split message #"\n")))))

(defn print-message [messages] 
  (println (join " " messages)))  