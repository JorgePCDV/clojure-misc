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

(defn -main [& args]
  (println (slurp filename)))
