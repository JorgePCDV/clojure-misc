(ns clojura.math)

(defn factorial [n]
  (if (= n 1) 1
              (* n (factorial (dec n)))))

(defn factorial-big-int [n]
  (if (= n 1) 1
              ('* n (factorial (dec n)))))