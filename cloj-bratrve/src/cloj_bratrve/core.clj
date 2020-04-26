(ns cloj-bratrve.core
(:gen-class))

(defn append-exclamation
  "Appends exclamation point"
  [param]
  (str "!" param "!"))

(defn multi-arity-example
  ([first-name family-name]
    (str "First: " first-name " Second: " family-name))
  ([family-name]
    (multi-arity-example "Default First Name" family-name)))

(defn destructuring [[first-choice second-choice & rest-choices]]
  (println (str "First choice is: " first-choice))
  (println (str "Second choice is: " second-choice))
  (println (str "Other choices are: " (clojure.string/join ", " rest-choices))))

(defn rest-param
  [name & things]
  (str name "'s things: " (clojure.string/join ", " things)))

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

