(ns hello_clojure.core
  (:gen-class)
  (:require [hello-clojure.math :refer [factorial]]))

(defn -main [& args]
  (println (factorial 5))
  (println "hello clojure"))
