(ns commit-formatter.ui
  (:use [clojure.string :only (split join)]
        [commit-formatter.core]
        [snipsnap.core])
  (:import (javax.swing JPanel)
           (javax.swing JLabel)
           (javax.swing JOptionPane))
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
  (doto (new javax.swing.JTextField header-length)
    (.setDocument (proxy [javax.swing.text.PlainDocument] []
                    (insertString [offset str attr]
                      (when  (and 
                               (not (nil? str))
                               (<= (+ (.getLength this) (.length str)) header-length))
                        (proxy-super insertString offset str attr)))))))
  
(defn format-and-copy-message [header-field text-area]
  (fn [] (let [formatted-message (format-message (.getText text-area))] 
           (.setText text-area formatted-message)
           (set-text! (format "%s%s%s" (.getText header-field) header-message-separator formatted-message)))))

(defn clear-message [frame header-field text-area]
  (fn []
    (when (= 0 (JOptionPane/showConfirmDialog 
                 frame "Are you sure?" "Clear text" JOptionPane/YES_NO_OPTION))
      (.setText header-field "")
      (.setText text-area ""))))


(defn paste-message [frame header-field text-area]
  (fn []
    (let [to-paste (get-text)]
      
      (if (validate-message to-paste)
        (let [{header :header message :message} (convert-message to-paste)]
          (do
            (.setText header-field header))
          (.setText text-area message))
        (JOptionPane/showConfirmDialog 
          frame 
          (format "Header must not be empty and cannot be longer than %d characters" header-length) 
          "Format error" JOptionPane/ERROR_MESSAGE)))))

(defn create-buttons-panel [paste-function format-function clear-function]
  (doto (new JPanel (new java.awt.FlowLayout) true)
    (.add (doto (new javax.swing.JButton "Paste")
            (on-action event (paste-function))))
    (.add (doto (new javax.swing.JButton "Format & copy")
            (on-action event (format-function))))
    (.add (doto (new javax.swing.JButton "Clear")
            (on-action event (clear-function))))))

(defn create-header-panel [header]
  (doto (new JPanel)
    (.add (new JLabel "Header:"))
    (.add header)))

(defn create-message-panel [message-area]
  (doto (new JPanel)
    (.add (new JLabel "Message:"))
    (.add (new javax.swing.JScrollPane message-area))))


(defn create-main-frame []
  (let [frame (create-frame "Commit formatter" 640 480)
        message-area (new javax.swing.JTextArea 30 50)
        header (create-header)]
  (doto frame
    (.add
      (doto (new JPanel (new java.awt.BorderLayout) true)
        (.add (create-header-panel header) (. java.awt.BorderLayout NORTH))
        (.add (create-message-panel message-area)
              (. java.awt.BorderLayout CENTER))
        (.add (create-buttons-panel 
                (paste-message frame header message-area)
                (format-and-copy-message header message-area)
                (clear-message frame header message-area))
              (. java.awt.BorderLayout SOUTH)))))))

(defn new-main-frame []
  (let [frame (create-main-frame)]
    (.pack frame)
    (.setVisible frame true)))

(defn -main [& args]
  (new-main-frame))