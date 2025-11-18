(ns aoc.2020.day3
  (:require
   [clojure.string :as string]
   [clojure.java.io :as io]))

(def input (-> "../input/2020/day3.txt" slurp))
(def sample-input (-> "../input/2020/day3-ex.txt" slurp))

(defn parse-input
  [input]
  (->> input
       string/split-lines))

(defn calculate-pos
  [data right down]
  (map vector
       (range down (count data) down)
       (iterate #(mod (+ right %) (count (first data))) right)))

(defn calculate-no-of-trees
  [data right down]
  (let [pos-to-check (calculate-pos data right down)]
    (->> pos-to-check
         (map (fn [pos] (get-in data pos)))
         (filter #(= % \#))
         count)))

(defn day3-part1
  [input]
  (let [data (parse-input input)]
    (calculate-no-of-trees data 3 1)))

(defn day3-part2
  [input]
  (let [data (parse-input input)]
    (apply *
           (map
            (fn [[right down]]
              (calculate-no-of-trees data right down))
            [[1 1] [3 1] [5 1] [7 1] [1 2]]))))
