(ns clojura.core
  (:gen-class)
  (:require [clojura.math :refer :all]))

(defn fizzbuzz [start finish]
  (map (fn [n]
         (cond
           (zero? (mod n 15)) "FizzBuzz"
           (zero? (mod n 3)) "Fizz"
           (zero? (mod n 5)) "Buzz"
           :else n))
       (range start finish)))

(defn -main [& args]
  (println (fizzbuzz 1 55))
  (println (factorial 5))
  (println (factorial-bigint 21)))
