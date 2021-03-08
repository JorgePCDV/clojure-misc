(ns cloj-bratrve.core
  (:gen-class))
(require '[clojure.string :as s])

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

(def anon-functions #(* % 3))

(defn incrementor
  "Creates a custom incrementor"
  [inc-by]
  #(+ % inc-by))
(def inc3 (incrementor 3))

(defn destructure-maps
  [{:keys [lat lng] :as location-map}]
  (println (str "Original map: " location-map))
  (println (str "Latitude provided: " lat))
  (println (str "Longitude provided: " lng)))

(def sum #(reduce + %))
(def avg #(/ (sum %) (count %)))
(defn stats
  [numbers]
  (map #(% numbers) [sum count avg]))

(def food-journal
  [{:month 1 :day 1 :pizza 5.3 :pasta 2.3}
   {:month 1 :day 2 :pizza 5.1 :pasta 2.0}
   {:month 2 :day 1 :pizza 4.9 :pasta 2.1}
   {:month 2 :day 2 :pizza 5.0 :pasta 2.5}
   {:month 3 :day 1 :pizza 4.2 :pasta 3.3}
   {:month 3 :day 2 :pizza 4.0 :pasta 3.8}
   {:month 4 :day 1 :pizza 3.7 :pasta 3.9}
   {:month 4 :day 2 :pizza 3.7 :pasta 3.6}])

(take-while #(< (:month %) 3) food-journal)
(drop-while #(< (:month %) 3) food-journal)
(take-while #(< (:month %) 4)
            (drop-while #(< (:month %) 2) food-journal))
(some #(and (> (:pasta %) 3) %) food-journal)

(defn append-function
  [arg]
  (str arg " appended"))

(defn even-numbers
  ([] (even-numbers 0))
  ([n] (cons n (lazy-seq (even-numbers (+ n 2))))))
(take 10 (even-numbers))

(concat [1 2] '(3 4))

(defn my-conj
  [target & additions]
  (into target additions))
(my-conj [0] 1 2 3)

(defn my-into
  [target additions]
  (apply conj target additions))
(my-into [0] [1 2 3])

(defn my-complement
  [fun]
  (fn [& args]
    (not (apply fun args))))
(def my-pos? (complement neg?))
(my-pos? 1)
(my-pos? -1)

(defn partial-logger
  [log-level message]
  (condp = log-level
    :warn (clojure.string/lower-case message)
    :emergency (clojure.string/upper-case message)))
(def warn (partial partial-logger :warn))
(def emergency (partial partial-logger :emergency))
(warn "SOME WARN MESSAGE")
(emergency "emergency message")

(defn sum-recur
  ([vals] (sum-recur vals 0))
  ([vals accumulating-total]
   (if (empty? vals)
     accumulating-total
     (recur (rest vals) (+ (first vals) accumulating-total)))))

(defn clean-string
  [text]
  (s/replace (s/trim text) #"( dirty|dirty )" ""))

;comp example
(def character
  {:name       "Some Character"
   :attributes {:intelligence 10
                :strength     4
                :dexterity    5}})
(def c-int (comp :intelligence :attributes))
(def c-str (comp :strength :attributes))
(def c-dex (comp :dexterity :attributes))
(defn spell-slots
  [char]
  (int (inc (/ (c-int char) 2))))
(def spell-slots-comp (comp int inc #(/ % 2) c-int))
(c-int character)
(c-str character)
(c-dex character)

; eval example
(def addition-list (list + 1 2))
(eval addition-list)

; clojure reader and evaluator
(read-string "(+ 1 2)")
(list? (read-string "(+ 1 2)"))
(conj (read-string "(+ 1 2)") :something)
(eval (read-string "(+ 1 2)"))
(read-string "#(+ 1 %)")
(read-string "'(a b c)")
(read-string "@var")
(read-string "; ignore!\n(+ 1 2)")

; macros
(defmacro ignore-last-operand
  [function-call]
  (butlast function-call))
(ignore-last-operand (+ 1 2 10))
(macroexpand '(ignore-last-operand (+ 1 2 10)))

; infix macro
(defmacro infix
  [infixed]
  (list (second infixed)
        (first infixed)
        (last infixed)))
(infix (1 + 2))

(defmacro print-and-return
  [expression]
  (list 'let ['result expression]
        (list 'println 'result)
        'result))

; threading macro
(defn read-resource
  [path]
  (-> path
      clojure.java.io/resource
      slurp
      read-string))

; syntax quoting/unquoting example
(defn criticize-code
  [criticism code]
  `(println ~criticism (quote ~code)))
(defmacro code-critic
  [bad good]
  `(do ~@(map #(apply criticize-code %)
             [["Bad code:" bad]
              ["Good code:" good]])))

(defmacro mischief-macro
  [& stuff-to-do]
  (let [macro-message (gensym 'message)]
    `(let [~macro-message "Mischievous"]
       ~@stuff-to-do
       (println "Mischievous macro is: " ~macro-message))))
(def message "Not mischievous")
(mischief-macro
  (println "Calling from mischief macro: " message))

(defn -main [& args]
  (foo "clojure"))
