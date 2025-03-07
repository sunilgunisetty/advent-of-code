(ns aoc.2024.day5
  (:require
   [clojure.string :as string]
   [clojure.java.io :as io]))

(defn parse-file
  [file]
  (as-> file $
    (slurp $)
    (string/split $ #"\n\n")
    (map string/split-lines $)))

(def sample-input (parse-file "../input/2024/day5-ex.txt"))
(def input (parse-file "../input/2024/day5.txt"))

(defn process-ordering
  [ordering]
  (reduce
   (fn [acc v]
     (let [[k v] (string/split v #"\|")]
       (update acc k #((fnil conj #{}) % v))))
   {} ordering))

(defn valid-update
  [orderings update]
  (loop [[x & xs] update
         res      false]
    (if (seq xs)
      (if (empty? (clojure.set/difference (into #{} xs) (get orderings x)))
        (recur xs true)
        false)
      res)))

(defn get-mid-val
  [update]
  (get update (int (/ (count update) 2))))

(defn day5-part1
  [input]
  (let [orderings (process-ordering (first input))]
    (->> input
         second
         (map #(string/split % #","))
         (filter #(valid-update orderings %))
         (map get-mid-val)
         (map #(Integer/parseInt %))
         (apply +))))

(defn make-comparator
  [orderings]
  (let [parsed-ordersings
        (reduce
         (fn [acc v]
           (conj acc (string/split v #"\|")))
         #{} orderings)]
    (fn [a b]
      (if (contains? parsed-ordersings [a b]) -1 1))))

(defn day5-part2
  [input]
  (let [comparator (make-comparator (first input))
        orderings  (process-ordering (first input))]
    (->> input
         second
         (map #(string/split % #","))
         (remove #(valid-update orderings %))
         (map #(sort comparator %))
         (map (fn [v] (get-mid-val (vec v))))
         (map #(Integer/parseInt %))
         (apply +))))
