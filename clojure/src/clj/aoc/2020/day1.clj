(ns aoc.2020.day1
  (:require
   [clojure.string :as string]
   [clojure.java.io :as io]))

(def input (-> "../input/2020/day1.txt" slurp))
(def sample-input (-> "../input/2020/day1-ex.txt" slurp))

(defn parse-input
  [input]
  (->> input
       string/split-lines
       (map parse-long)
       (into #{})))

(defn day1-part1
  [input]
  (let [data (parse-input input)]
    (first
     (for [i data
           :let [j (- 2020 i)]
           :when (< i j)
           :when (contains? data j)]
       (* i j)))))

(defn day1-part2
  [input]
  (let [data (parse-input input)]
    (first
     (for [i data
           j data
           :let [k (- 2020 i j)]
           :when (< i j k)
           :when (contains? data k)]
       (* i j k)))))
