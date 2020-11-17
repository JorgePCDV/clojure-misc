(ns namespaces-sample.core
  (:require [namespaces-sample.visualization.svg :as svg])
  (:gen-class))

(def data [{:location    "Cologne, Germany"
            :cheese-name "Archbishop Hildebold's Cheese Pretzel"
            :lat         50.95
            :lng         6.97}
           {:location    "Zurich, Switzerland"
            :cheese-name "The Standard Emmental"
            :lat         47.37
            :lng         8.55}
           {:location    "Marseille, France"
            :cheese-name "Le Fromage de Cosquer"
            :lat         43.30
            :lng         5.37}
           {:location    "Zurich, Switzerland"
            :cheese-name "The Lesser Emmental"
            :lat         47.37
            :lng         8.55}
           {:location    "Vatican City"
            :cheese-name "The Cheese of Turin"
            :lat         41.90
            :lng         12.45}])

(defn -main
  [& args]
  (println (svg/points data)))

