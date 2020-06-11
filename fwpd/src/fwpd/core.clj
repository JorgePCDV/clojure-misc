(ns fwpd.core)

(def filename "suspects.csv")

(def vamp-keys [:name :glit-index])

(defn str->int
  [str]
  (Integer. str))

(def conversions {:name identity :glit-index str->int})

(defn convert
  [vamp-key value]
  ((get conversions vamp-key) value))

(defn parse
  "Convert a CSV into rows of columns"
  [string]
  (map #(clojure.string/split % #",")
       (clojure.string/split string #"\n")))

(defn mapify
  "Return a seq of maps like {:name \"Jorge\" :glit-index 10}"
  [rows]
  (map (fn [unmapped-row]
         (reduce (fn [row-map [vamp-key value]]
                   (assoc row-map vamp-key (convert vamp-key value)))
                 {}
                 (map vector vamp-keys unmapped-row)))
       rows))

(defn glit-filter
  [min-glit records]
  (filter #(>= (:glit-index %) min-glit) records))

(defn glit-filter-names
  [min-glit records]
  (map (fn [record] (:name record)) (filter #(>= (:glit-index %) min-glit) records)))

(defn analysis
  [text]
  (str "Character count: " (count text)))

(defn analyze-file
  [filename]
  (analysis (slurp filename)))

(defn -main [& args]
  (println (parse (slurp filename))))
