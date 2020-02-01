(ns clojura.math-test
  (:require [clojure.test :refer :all])
  (:require [clojura.math :refer :all]))

(deftest factorial-test
  (testing "5 factorial"
    (is (= 120 (factorial 5)))))

(deftest factorial-big-int-test
  (testing "21 factorial"
    (is (= 2432902008176640000 (factorial-big-int 21)))))
