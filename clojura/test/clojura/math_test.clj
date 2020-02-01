(ns clojura.math-test
  (:require [clojure.test :refer :all])
  (:require [clojura.math :refer [factorial]]))

(deftest factorial-test
  (testing "5 factorial"
    (is (= 120 (factorial 5)))))
