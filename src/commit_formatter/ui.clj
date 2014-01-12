(ns ui
  (:use [clojure.string :only (split join)]
        [commit-formatter.core :only (format-message)]
        [snipsnap.core]))

(defmacro on-action [component event & body]
  `(. ~component addActionListener
      (proxy [java.awt.event.ActionListener] []
        (actionPerformed [~event] ~@body))))

(defn create-frame [title size-x size-y]
  (doto(new javax.swing.JFrame)
    (.setSize size-x size-y)
    (.setTitle title)))

(defn update-text-area [text-area]
  (let [formatted-message (format-message (.getText text-area))] 
    (.setText text-area formatted-message)
    (set-text! formatted-message)))

(def header (new javax.swing.JTextField 52))

(defn create-main-frame []
  (let [message-area (new javax.swing.JTextArea)]
  (doto (create-frame "Commit formatter" 640 480)
    (.add
      (doto (new javax.swing.JPanel (new java.awt.BorderLayout) true)
        (.add header (. java.awt.BorderLayout NORTH))
        (.add  message-area
              (. java.awt.BorderLayout CENTER))
        (.add (doto (new javax.swing.JButton "Format & copy")
                (on-action event (update-text-area message-area)))
              (. java.awt.BorderLayout SOUTH)))))))



(defn new-main-frame []
  (let [frame (create-main-frame)]
    (.setVisible frame true)))