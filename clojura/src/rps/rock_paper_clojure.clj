(ns rps.rock-paper-clojure
  (:gen-class))

(defn get-guess []
  (println "Pick one: (r)ock, (p)aper, (s)cissors")
  (let [guess (read-line)]
    (if (get {"r" true "p" true "s" true} guess) guess)))

(defn -main [& args]
  (println (get-guess)))
