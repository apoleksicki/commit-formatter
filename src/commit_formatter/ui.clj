(ns commit-formatter.ui
  (:use [clojure.string :only (split join)]
        [commit-formatter.core :only (format-message header-length)]
        [snipsnap.core])
  (:gen-class))

(defmacro on-action [component event & body]
  `(. ~component addActionListener
      (proxy [java.awt.event.ActionListener] []
        (actionPerformed [~event] ~@body))))

(defn create-frame [title size-x size-y]
  (doto(new javax.swing.JFrame)
    (.setSize size-x size-y)
    (.setTitle title)))

(defn create-header []
  (println "create-header")
  (doto (new javax.swing.JTextField header-length)
    (.setDocument (proxy [javax.swing.text.PlainDocument] []
                    (insertString [offset str attr]
                      (when  (and 
                               (not (nil? str))
                               (<= (+ (.getLength this) (.length str)) header-length))
                        (proxy-super insertString offset str attr)))))))
  
(defn format-and-copy-message [header-field text-area]
  (let [formatted-message (format-message (.getText text-area))] 
    (.setText text-area formatted-message)
    (set-text! (format "%s\n\n%s" (.getText header-field) formatted-message))))

(defn create-main-frame []
  (let [message-area (new javax.swing.JTextArea)
        header (create-header)]
  (doto (create-frame "Commit formatter" 640 480)
    (.add
      (doto (new javax.swing.JPanel (new java.awt.BorderLayout) true)
        (.add header (. java.awt.BorderLayout NORTH))
        (.add  message-area
              (. java.awt.BorderLayout CENTER))
        (.add (doto (new javax.swing.JButton "Format & copy")
                (on-action event (format-and-copy-message header message-area)))
              (. java.awt.BorderLayout SOUTH)))))))


(defn new-main-frame []
  (let [frame (create-main-frame)]
    (.setVisible frame true)))

(defn -main [& args]
  (new-main-frame))