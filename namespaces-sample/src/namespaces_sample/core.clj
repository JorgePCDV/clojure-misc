(ns namespaces-sample.core
  (:gen-class))
(require 'namespaces-sample.visualization.svg)
(refer 'namespaces-sample.visualization.svg)

(defn -main
  [& args]
  (foo "namespaces exercice"))

