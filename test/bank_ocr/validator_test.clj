(ns bank-ocr.validator-test
  (:require [clojure.test :refer :all]
            [bank-ocr.validator :refer :all]))

(deftest valid-checksums
  (are [result] (= 0 (mod result 11))
       (checksum [0 0 0 0 0 0 0 5 1])
       (checksum [3 4 5 8 8 2 8 6 5])
       (checksum [4 5 7 5 0 8 0 0 0])))

(deftest invalid-checksums
  (are [result] (not (= 0 (mod result 11)))
       (checksum [1 2 3 4 5 6 7 8 0])
       (checksum [6 6 4 3 7 1 4 9 5])
       (checksum [9 8 7 6 5 4 3 2 1])))

(deftest valid-account-numbers
  (are [-vector] (valid? -vector)
       [0 0 0 0 0 0 0 5 1]
       [3 4 5 8 8 2 8 6 5]
       [4 5 7 5 0 8 0 0 0]))

(deftest invalid-account-numbers
  (are [-vector] (not (valid? -vector))
       [1 2 3 4 5 6 7 8 0]
       [6 6 4 3 7 1 4 9 5]
       [9 8 7 6 5 4 3 2 1]
       [0 0 0 0 0 0 \? 5 1]))

(deftest legibility
  (is (legible? [0 0 0 0 0 0 0 5 1]))
  (is (not (legible? [0 0 0 0 0 0 \? 5 1]))))

(deftest describe-validity
  (are [result -vector]
       (= result (error-description -vector))
       nil   [0 0 0 0 0 0 0 5 1]
       "ERR" [6 6 4 3 7 1 4 9 5]
       "ILL" [0 0 0 0 0 0 \? 5 1]))
