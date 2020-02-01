(ns clojura.core
  (:gen-class)
  (:require [clojura.math :refer [factorial]]))

(defn -main [& args]
  (println (factorial 5))
  (println "hello clojure"))
