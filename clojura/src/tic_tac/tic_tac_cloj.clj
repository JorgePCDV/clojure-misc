(ns tic_tac.tic-tac-cloj
  (:gen-class))

(defn triple-winner? [triple]
  (if (every? #{:x} triple) :x
    (if (every? #{:o} triple) :o)))

(declare triples)

(defn winner? [board]
  (first (filter #{:x :o} (map triple-winner? (triples board)))))

(defn -main [& args])
