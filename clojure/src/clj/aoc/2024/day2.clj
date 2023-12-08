(ns aoc.2024.day2
  (:require
   [clojure.string :as string]
   [clojure.java.io :as io]))

(def sample-input (->> "input/2024/day2-sample.txt" io/resource slurp string/split-lines))
(def input (->> "input/2024/day2.txt" io/resource slurp string/split-lines))

(defn parse-reports
  [input]
  (->> input
       (map #(string/split % #"\s"))
       (map #(map (fn [v] (Integer/parseInt v)) %))))

(defn increasing?
  [xs]
  (apply < xs))

(defn decreasing?
  [xs]
  (apply > xs))

(defn valid-adj-level?
  [xs]
  (->> xs
       (partition-all 2 1)
       drop-last
       (map #(apply - %))
       (every? #(< (abs %) 4))))

(defn valid-report?
  [xs]
  (and (or (increasing? xs) (decreasing? xs)) (valid-adj-level? xs)))

(defn day2-part1
  [input]
  (->> input
       parse-reports
       (filter valid-report?)
       count))

(defn remove-at
  [pos coll]
  (into (subvec coll 0 pos) (subvec coll (inc pos))))

(defn valid-report-with-tolerance?
  [xs]
  (or (valid-report? xs)
      (some valid-report? (map #(remove-at % (vec xs)) (range (count xs))))))

(defn day2-part2
  [input]
  (->> input
       parse-reports
       (filter valid-report-with-tolerance?)
       count))
