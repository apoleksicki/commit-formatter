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
           (set-text! (format "%s\n\n%s" (.getText header-field) formatted-message)))))

(defn clear-message [frame header-field text-area]
  (fn []
    (when (= 0 (javax.swing.JOptionPane/showConfirmDialog 
                 frame "Are you sure?" "Clear text" javax.swing.JOptionPane/YES_NO_OPTION))
      (.setText header-field "")
      (.setText text-area ""))))

(defn create-buttons-panel [format-function clear-function]
  (doto (new javax.swing.JPanel (new java.awt.FlowLayout) true)
    (.add (doto (new javax.swing.JButton "Format & copy")
            (on-action event (format-function))))
    (.add (doto (new javax.swing.JButton "Clear")
            (on-action event (clear-function))))))

(defn create-main-frame []
  (let [message-area (new javax.swing.JTextArea)
        header (create-header)
        frame (create-frame "Commit formatter" 640 480)]
  (doto frame
    (.add
      (doto (new javax.swing.JPanel (new java.awt.BorderLayout) true)
        (.add header (. java.awt.BorderLayout NORTH))
        (.add  message-area
              (. java.awt.BorderLayout CENTER))
        (.add (create-buttons-panel 
                (format-and-copy-message header message-area)
                (clear-message frame header message-area))
              (. java.awt.BorderLayout SOUTH)))))))

(defn new-main-frame []
  (let [frame (create-main-frame)]
    (.setVisible frame true)))

(defn -main [& args]
  (new-main-frame))