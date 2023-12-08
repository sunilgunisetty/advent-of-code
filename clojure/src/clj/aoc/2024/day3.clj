(ns aoc.2023.day3
  (:require
   [clojure.string :as string]
   [clojure.java.io :as io]))

(def sample-input (->> "input/2024/day3-sample.txt" io/resource slurp string/split-lines))
(def input (->> "input/2024/day3.txt" io/resource slurp string/split-lines))
(def sample-input-2 ["xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"])

(def instruction #"mul\(([0-9]{1,3}),([0-9]{1,3})\)")
(def valid-instruction #"don\'t\(\).*?do\(\)")

(defn calculate
  [instructions]
  (->> instructions
       (map (fn [[_ a b]] (* (Integer/parseInt a) (Integer/parseInt b))))
       (apply +)))

(defn day3-part1
  [input]
  (->> input
       (string/join "")
       (re-seq instruction)
       calculate))

(defn day3-part2
  [input]
  (as-> input $
    (string/join "" $)
    (string/replace $ valid-instruction "")
    (re-seq instruction $)
    (calculate $)))
