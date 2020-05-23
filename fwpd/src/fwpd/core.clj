(ns fwpd.core)
(def filename "suspects.csv")

(defn -main [& args]
  (println (slurp filename)))
