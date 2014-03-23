(ns commit-formatter.ui
  (:use [clojure.string :only (split join)]
        [clojure.repl]
        [commit-formatter.core]
        [snipsnap.core]
        [seesaw.core])
  (:import (javax.swing JOptionPane))
  (:gen-class))

(defn create-header []
  (doto (text :multi-line? false :columns header-length)
    (.setDocument (proxy [javax.swing.text.PlainDocument] []
                    (insertString [offset str attr]
                      (when  (and 
                               (not (nil? str))
                               (<= (+ (.getLength this) (.length str)) header-length))
                        (proxy-super insertString offset str attr)))))))
  
(defn format-and-copy-message [header-field text-area]
  (fn [e] (let [formatted-message (format-message (.getText text-area))] 
           (text! text-area formatted-message)
           (set-text! (format "%s%s%s" (.getText header-field) header-message-separator formatted-message)))))

(defn clear-message [frame header-field text-area]
  (fn [e]
    (when (= 0 (JOptionPane/showConfirmDialog 
                 frame "Are you sure?" "Clear text" JOptionPane/YES_NO_OPTION))
      (text! header-field "")
      (text! text-area ""))))


(defn paste-message [frame header-field text-area]
  (fn [e]
    (let [to-paste (get-text)]
      
      (if (validate-message to-paste)
        (let [{header :header message :message} (convert-message to-paste)]
          (do
            (text! header-field header))
          (text! text-area message))
        (JOptionPane/showMessageDialog 
          frame 
          (format "Header must not be empty and cannot be longer than %d characters" header-length) 
          "Format error" JOptionPane/ERROR_MESSAGE)))))

(defn create-button [text function]
  (doto (button :text text)
    (listen :action function)))

(defn create-buttons-panel [paste-function format-function clear-function]
  (flow-panel 
    :items (list 
             (create-button "Paste" paste-function)
             (create-button "Format & copy" format-function)
             (create-button "Clear" clear-function))))

(defn create-header-panel [header]
  (flow-panel 
    :items (list (label "Header:") header)))

(defn create-message-panel [message-area]
  (flow-panel
    :items (list(label "Message:") (scrollable message-area))))


(defn create-message-area []
 (text :multi-line? true :wrap-lines? true :rows 30 :columns line-length))

(defn create-main-frame []
  (let [message-area (create-message-area)
        header (create-header)]
    (frame :title "Commit formatter" 
           :on-close :dispose
           :content (border-panel
                      :north (create-header-panel header) 
                      :center (create-message-panel message-area)
                      :south      
                      (create-buttons-panel 
                        (paste-message frame header message-area)
                        (format-and-copy-message header message-area)
                        (clear-message frame header message-area))))))

(defn new-main-frame []
  (-> (create-main-frame)
    pack!
    show!))

(defn -main [& args]
  (new-main-frame))