(ns clojura.math)

(defn factorial [n]
  (if (= n 1) 1
              (* n (factorial (dec n)))))

(defn factorial-bigint [n]
  (if (= n 1) 1
              (*' n (factorial (dec n)))))

(defn factorial-bigint-loop [n]
    (if (= n 0) 1
                (loop [val n i n]
                  (if (<= i 1) val
                               (recur (*' val (dec i)) (dec i))))))