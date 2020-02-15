(ns tic_tac.tic-tac-cloj-test
  (:require [clojure.test :refer :all])
  (:require [tic_tac.tic-tac-cloj :refer :all]))

(deftest triple-winner?-test-o
  (testing "Triple winner :o"
    (is (= :o (triple-winner? [:o :o :o])))))

(deftest triple-winner?-test-x
  (testing "Triple winner :x"
    (is (= :x (triple-winner? [:x :x :x])))))

(deftest triple-winner?-test-nil
  (testing "Triple winner nil"
    (is (= nil (triple-winner? [:x :o :x])))))