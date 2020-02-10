(ns rps.rock-paper-clojure
  (:gen-class))

(defn get-guess []
  (println "Pick one: (r)ock, (p)aper, (s)cissors")
  (let [guess (read-line)]
    (if (get {"r" true "p" true "s" true} guess) guess)))

(defn winner [guess1 guess2]
  (let [guesses [guess1 guess2]]
    (cond
      (= guess1 guess2) 0
      (= guesses ["p" "r"]) 1
      (= guesses ["r" "p"]) 2
      (= guesses ["r" "s"]) 1
      (= guesses ["s" "r"]) 2
      (= guesses ["s" "p"]) 1
      (= guesses ["p" "s"]) 2)))

(defn -main [& args]
  (println (get-guess)))
