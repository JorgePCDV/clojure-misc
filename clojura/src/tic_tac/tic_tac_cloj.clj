(ns tic_tac.tic-tac-cloj
  (:gen-class))

(defn triple-winner? [triple]
  (if (every? #{:x} triple) :x
    (if (every? #{:o} triple) :o)))

(defn -main [& args])
