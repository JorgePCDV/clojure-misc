(ns cloj-bratrve.core
(:gen-class))

(defn foo
  "I don't do a whole lot."
  [x]
  (println "Hello, " x))

(defn -main [& args]
  (foo "clojure"))

