(ns tic_tac.tic-tac-cloj
  (:gen-class))

(defn triple-winner? [triple]
  (if (every? #{:x} triple) :x
    (if (every? #{:o} triple) :o)))

(declare triples)

(defn winner? [board]
  (first (filter #{:x :o} (map triple-winner? (triples board)))))

(defn triples [board]
  (concat
    (partition-all 3 board)
    (list
        (take-nth 3 board)
        (take-nth 3 (drop 1 board))
        (take-nth 3 (drop 2 board)))))

(defn -main [& args])
