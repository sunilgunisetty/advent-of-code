(ns aoc.2024.day7
  (:require
   [clojure.string :as string]
   [clojure.java.io :as io]))

(defn process-equation
  [equation]
  (let [[total digits] (string/split equation #":")]
    [(parse-long total)
     (map (fn [v] (-> v string/trim parse-long)) (string/split (string/trim digits) #" "))]))

(def sample-input (->> "../input/2024/day7-sample.txt" slurp string/split-lines (map process-equation)))
(def input (->> "../input/2024/day7.txt" slurp string/split-lines (map process-equation)))

(defn valid-eq-part-1?
  [total running-total [y & ys]]
  (if (> running-total total)
    false
    (let [a (+ running-total y)
          b (* running-total y)]
      (if (seq ys)
        (or (valid-eq-part-1? total a ys)
            (valid-eq-part-1? total b ys))
        (or (= total a) (= total b))))))

(defn valid-eq-part-2?
  [total running-total [y & ys]]
  (if (> running-total total)
    false
    (let [a (+ running-total y)
          b (* running-total y)
          c (parse-long (str (str running-total) (str y)))]
      (if (seq ys)
        (or (valid-eq-part-2? total a ys)
            (valid-eq-part-2? total b ys)
            (valid-eq-part-2? total c ys))
        (or (= total a) (= total b) (= total c))))))

(defn day7-part1
  [input]
  (->> input
       (filter
        (fn [[total nums]]
          (valid-eq-part-1? total (first nums) (rest nums))))
       (map first)
       (apply +)))

(defn day7-part2
  [input]
  (->> input
       (filter
        (fn [[total nums]]
          (valid-eq-part-2? total (first nums) (rest nums))))
       (map first)
       (apply +)))
