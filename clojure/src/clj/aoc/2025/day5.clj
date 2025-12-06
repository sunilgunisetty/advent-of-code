(ns aoc.2025.day5
  (:require
   [clojure.string :as string]
   [clojure.java.io :as io]))

(def input (-> "../input/2025/day5.txt" slurp))
(def sample-input (-> "../input/2025/day5-ex.txt" slurp))

(defn merge-intervals
  [intervals]
  (reduce
   (fn [acc [next-start next-end]]
     (let [[prev-start prev-end] (last acc)]
       (cond
         (and
          (<= next-start prev-end)
          (>= next-end prev-end)) (conj (pop acc) [prev-start next-end])

         (and
          (<= next-start prev-end)
          (<= next-end prev-end)) (conj (pop acc) [prev-start prev-end])

         :else
         (conj acc [next-start next-end]))))
   [(first intervals)]
   (rest intervals)))

(defn process-input
  [input]
  (let [[fresh-ingredients available-ingredients] (string/split input #"\n\n")]
    [(->> fresh-ingredients
          string/split-lines
          (map
           (fn [val]
             (let [[s e] (string/split val #"-")]
               [(parse-long s) (parse-long e)])))
          (sort-by first))
     (->> available-ingredients string/split-lines (map parse-long))]))

(defn day5-part1
  [input]
  (let [[fresh-ingredients available-ingredients] (process-input input)
        merged-intervals (merge-intervals fresh-ingredients)]
    (->> available-ingredients
         (map (fn [ingredient]
                (some (fn [[s e]]
                        (and (>= ingredient s) (<= ingredient e)))
                      merged-intervals)))
         (filter some?)
         count)))

(defn day5-part2
  [input]
  (let [fresh-ingredients (->> input process-input first)
        merged-intervals (merge-intervals fresh-ingredients)]
    (reduce (fn [acc [start end]] (+ acc (- end start) 1)) 0 merged-intervals)))
