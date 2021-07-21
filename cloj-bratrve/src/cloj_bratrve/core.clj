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

; avoid double evaluation
(defmacro report
  [to-try]
  `(let [result# ~to-try]
     (if result#
       (println (quote ~to-try) "was successful:" result#)
       (println (quote ~to-try) "was not successful:" result#))))
(report (do (Thread/sleep 1000) (+ 1 1)))

; macro composition (careful with this approach)
(defmacro doseq-macro
  [macroname & args]
  `(do
     ~@(map (fn [arg] (list macroname arg)) args)))
(doseq-macro report (= 1 1) (= 1 2))

;; futures
(let [result (future (Thread/sleep 3000)
                     (+ 1 1))]
  (println "Dereferenced result is: " @result)
  (println "It will be at least 3 seconds before printing this"))

(let [f (future)]
  @f
  (realized? f))

;; delays
(def delay-example
  (delay (let [message "This is a delay example"]
           (println "First deref:" message)
           message)))
(force delay-example)
@delay-example

;; delay use case
(def uploads ["one.jpg" "two.jpg" "three.jpg"])
(defn email-user
  [email-address]
  (println "Sending notification to" email-address))
(defn upload-document
  "Needs to be implemented"
  [document]
  true)
(let [notify (delay (email-user "example_user@gmail.com"))]
  (doseq [document uploads]
    (future (upload-document document)
            (force notify))))

;; promises
(def my-promise (promise))
(deliver my-promise (+ 1 2))
@my-promise

;; other promise example
(def product-one
  {:store   "Store One"
   :price   90
   :quality 90})
(def product-two
  {:store   "Store Two"
   :price   150
   :quality 83})
(def product-three
  {:store   "Store Three"
   :price   94
   :quality 99})
(defn mock-api-call
  [result]
  (Thread/sleep 1000)
  result)
(defn satisfactory?
  "If product meets criteria, return product,  else return false"
  [product]
  (and (<= (:price product) 100)
       (>= (:quality product) 97)
       product))
(time (some (comp satisfactory? mock-api-call)
            [product-one product-two product-three]))
(time
  (let [product-promise (promise)]
    (doseq [product [product-one product-two product-three]]
      (future (if-let [satisfactory-product (satisfactory? (mock-api-call product))]
                (deliver product-promise satisfactory-product))))
    (println "Product chosen is: " @product-promise)))

;; promise timeout
(let [p (promise)]
  (deref p 100 "timed out"))

;; callbacks in clojure
(let [callback-promise (promise)]
  (future (println "Here's callback promise: " @callback-promise))
  (Thread/sleep 100)
  (deliver callback-promise "Callback!"))

;; rolling out own queue
(defmacro wait
  "Sleep `timeout` seconds before evaluating body"
  [timeout & body]
  `(do (Thread/sleep ~timeout) ~@body))
(defmacro enqueue
  ([q concurrent-promise-name concurrent serialized]
   `(let [~concurrent-promise-name (promise)]
      (future (deliver ~concurrent-promise-name ~concurrent))
      (deref ~q)
      ~serialized
      ~concurrent-promise-name))
  ([concurrent-promise-name concurrent serialized]
   `(enqueue (future) ~concurrent-promise-name ~concurrent ~serialized)))
(-> (enqueue item (wait 200 "Queueing item 1") (println @item))
    (enqueue item (wait 400 "Queueing item 2") (println @item))
    (enqueue item (wait 100 "Queueing item 3") (println @item)))

;; atoms
(def my-atom (atom {:attribute-one 10
                    :attribute-two 60}))
(let [my-atom-state @my-atom]
  (if (>= (:attribute-two my-atom-state) 50)
    (future (println (:attribute-one my-atom-state)))))

;; atom swap! (increase values of my-atom)
(swap! my-atom (fn [current-state]
                 (merge-with + current-state {:attribute-one 1
                                              :attribute-two 5})))
(defn increase-my-atom-level
  [atom-state increase-by]
  (merge-with + atom-state {:attribute-one increase-by}))
(swap! my-atom increase-my-atom-level 10)
;; same as above
(swap! my-atom update-in [:attribute-one] + 10)

;; retrieving past atom states
(let [num (atom 1)
      s1 @num]
  (swap! num inc)
  (println "State 1:" s1)
  (println "Current state:" @num))

;; reset atom
(reset! my-atom {:attribute-one 0
                 :attribute-two 0})

;; watches
(defn calculate-attribute
  [an-atom]
  (* (:attribute-one an-atom)
     (- 100 (:attribute-two an-atom))))
(defn atom-alert
  [key watched old-state new-state]
  (let [watched-attribute (calculate-attribute new-state)]
    (if (> watched-attribute 5000)
      (do
        (println "Alert!")
        (println "Watched attribute above: " watched-attribute)
        (println "Message published by: " key))
      (do
        (println "All good with key: " key)
        (println "Attribute one: " (:attribute-one new-state))
        (println "Attribute two: " (:attribute-two new-state))
        (println "Watched attribute: " watched-attribute)))))
(reset! my-atom {:attribute-one 22
                 :attribute-two 2})
(add-watch my-atom :my-alert atom-alert)
(swap! my-atom update-in [:attribute-two] + 1)
(swap! my-atom update-in [:attribute-one] + 30)

;; validators
(defn attribute-two-validator
  [{:keys [attribute-two]}]
  (or (and (>= attribute-two 0)
           (<= attribute-two 100))
      (throw (IllegalStateException. "attribute-two cannot exceed 100"))))
(def validated-atom
  (atom {:attribute-one 0 :attribute-two 0}
        :validator attribute-two-validator))
(swap! validated-atom update-in [:attribute-two] + 50)

;; references
(def varieties
  #{"one" "two" "three" "four" "five"})
(defn variety-count
  [variety count]
  {:variety variety
   :count count})
(defn generate-variety-state
  [name]
  {:name name
   :varieties #{}})

(def first-variety (ref (generate-variety-state "First variety")))
(def variety-holder (ref {:name "Holder"
                          :varieties (set (map #(variety-count % 2) varieties))}))

(defn reassign-varieties
  [first-variety holder]
  (dosync
    (when-let [pair (some #(if (= (:count %) 2) %) (:varieties @holder))]
      (let [updated-count (variety-count (:variety pair) 1)]
        (alter first-variety update-in [:varieties] conj updated-count)
        (alter holder update [:varieties] disj pair)
        (alter holder update-in [:varieties] conj updated-count)))))
(reassign-varieties first-variety variety-holder)
(:varieties @first-variety)
(defn similar-varieties
  [target-variety variety-set]
  (filter #(= (:variety %) (:variety target-variety)) variety-set))
(similar-varieties (first (:varieties @first-variety)) (:varieties @variety-holder))


;; commutte example
(defn sleep-print-update
  [sleep-time thread-name update-fn]
  (fn [state]
    (Thread/sleep sleep-time)
    (println (str thread-name ": " state))
    (update-fn state)))
(def counter (ref 0))
(future (dosync (commute counter (sleep-print-update 100 "Thread A" inc))))
(future (dosync (commute counter (sleep-print-update 100 "Thread B" inc))))



(defn -main [& args]
  (foo "clojure"))