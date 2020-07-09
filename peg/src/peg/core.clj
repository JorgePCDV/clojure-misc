(ns peg.core
  (:require [clojure.set :as set])
  (:gen-class))

(defn get-input
  "Waits for user to enter text and hit enter, then cleans input"
  ([] (get-input ""))
  ([default]
   (let [input (clojure.string/trim (read-line))]
     (if (empty? input)
       default
       (clojure.string/lower-case input)))))

(defn prompt-rows
  []
  (println "How many rows? [5]")
  (let [rows (Integer. (get-input 5))
        board (new-board rows)]
    (prompt-empty-peg board)))

(defn -main
  [& args]
  (println "Get ready to play!")
  (prompt-rows))
