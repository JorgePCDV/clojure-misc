(ns cloj-bratrve.core
(:gen-class))

(defn foo
  "I don't do a whole lot."
  [x]
  (println "Hello, " x))

(defn error-message
  [severity]
  (str "Displaying error message: "
       (if (= severity :mild)
         "mild error"
         "alert")))

(defn -main [& args]
  (foo "clojure"))

