(ns clojura.core
  (:gen-class)
  (:require [clojura.math :refer :all]))

(defn -main [& args]
  (println "hello clojura")
  (println (factorial 5))
  (println (factorial-big-int 21)))
