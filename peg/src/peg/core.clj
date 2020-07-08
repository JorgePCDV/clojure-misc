(ns peg.core
  (require [clojure.set :as set])
  (:gen-class))

(defn prompt-rows
  []
  (println "How many rows? [5]"))

(defn -main
  [& args]
  (println "Get ready to play!")
  (prompt-rows))
