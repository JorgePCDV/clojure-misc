(ns clojura.math-test
  (:require [clojure.test :refer :all])
  (:require [clojura.math :refer :all]))

(deftest factorial-test
  (testing "5 factorial"
    (is (= 120 (factorial 5)))))

(deftest factorial-big-int-test
  (testing "21 factorial"
    (is (= 51090942171709440000N (factorial-bigint 21)))))

(deftest factorial-bigint-loop-test
  (testing "21 factorial big int loop"
    (is (= 51090942171709440000N (factorial-bigint-loop 21)))))
