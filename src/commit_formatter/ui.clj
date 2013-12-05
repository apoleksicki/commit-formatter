(ns ui
  (:use [clojure.string :only (split)]))

(defn create-frame [title size-x size-y]
  (doto(new javax.swing.JFrame)
    (.setSize size-x size-y)
    (.setTitle title)))

(def header (new javax.swing.JTextField 52))

(defn create-main-frame []
  (doto (create-frame "Commit formatter" 640 480)
    (.add
      (doto (new javax.swing.JPanel (new java.awt.BorderLayout) true)
        (.add header (. java.awt.BorderLayout NORTH))
        (.add (new javax.swing.JTextArea) 
              (. java.awt.BorderLayout CENTER))
        (.add (new javax.swing.JButton "Format & copy") 
              (. java.awt.BorderLayout SOUTH))))))

(defn new-main-frame []
  (let [frame (create-main-frame)]
    (.setVisible frame true)))