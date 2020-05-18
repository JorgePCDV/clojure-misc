(ns cloj-bratrve.exercice2)

(def data
  {0 {:makes-puns? false, :has-pulse? true :name "McFishwich"}
   1 {:makes-puns? false, :has-pulse? true :name "McMackson"}
   2 {:makes-puns? true, :has-pulse? false :name "Damon Salvatore"}
   3 {:makes-puns? true, :has-pulse? true :name "Mickey Mouse"}})

(defn related-details
  [id]
  (Thread/sleep 1000)
  (get data id))

(defn vampire?
  [record]
  (and (:makes-puns? record)
       (not (:has-pulse? record))
       record))

(defn identify
  [ids]
  (first (filter vampire?
                 (map related-details ids))))

(defn -main [& args]
  (println (identify [0 1 2 3])))