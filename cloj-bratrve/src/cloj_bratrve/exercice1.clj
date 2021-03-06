(ns cloj-bratrve.exercice1)

(def asym-parts [{:name "head" :size 3}
                 {:name "left-eye" :size 1}
                 {:name "left-ear" :size 1}
                 {:name "mouth" :size 1}
                 {:name "nose" :size 1}
                 {:name "neck" :size 2}
                 {:name "left-shoulder" :size 3}
                 {:name "left-upper-arm" :size 3}
                 {:name "chest" :size 10}
                 {:name "back" :size 10}
                 {:name "left-forearm" :size 3}
                 {:name "abdomen" :size 6}
                 {:name "left-kidney" :size 1}
                 {:name "left-hand" :size 2}
                 {:name "left-knee" :size 2}
                 {:name "left-thigh" :size 4}
                 {:name "left-lower-leg" :size 3}
                 {:name "left-achilles" :size 1}
                 {:name "left-foot" :size 2}])

(defn matching-part
  [part]
  {:name (clojure.string/replace (:name part) #"^left-" "right-")
   :size (:size part)})

(defn symmetrize-parts
  "Expects a seq of maps that have a :name and :size"
  [asymmetric-parts]
  (loop [remaining-asym-parts asymmetric-parts
         final-parts []]
    (if (empty? remaining-asym-parts)
      final-parts
      (let [[part & remaining] remaining-asym-parts]
        (recur remaining
               (into final-parts (set [part (matching-part part)])))))))

(defn better-symmetrize-parts
  "Expects a seq of maps that have a :name and :size"
  [asym-parts]
  (reduce (fn [final-parts part]
            (into final-parts (set [part (matching-part part)])))
          []
          asym-parts))

(defn hit
  [asym-parts]
  (let [sym-parts (better-symmetrize-parts asym-parts)
        part-size-sum (reduce + (map :size sym-parts))
        target (rand part-size-sum)]
    (loop [[part & remaining] sym-parts
           accumulated-size (:size part)]
      (if (> accumulated-size target)
        part
        (recur remaining (+ accumulated-size (:size (first remaining))))))))

(defn -main [& args]
  (println (hit asym-parts)))