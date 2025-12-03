(ns aoc.2025.day2
  (:require
   [clojure.string :as string]
   [clojure.java.io :as io]))

(def input (-> "../input/2025/day2.txt" slurp string/trim-newline))
(def sample-input (-> "../input/2025/day2-ex.txt" slurp string/trim-newline))

(defn parse-input
  [input]
  (->> (string/split input #",")
       (map #(string/split % #"-"))
       (map (fn [[start end]] [(parse-long start) (parse-long end)]))))

(defn invalid-part1?
  [number]
  (let [number-string (str number)
        number-length (count number-string)]
    (and (even? number-length)
         (= (subs number-string 0 (/ number-length 2))
            (subs number-string (/ number-length 2))))))

(defn invalid-part2?
  [number]
  (let [number-string (str number)
        number-length (count number-string)]
    (->> (range 1 (inc (int (/ number-length 2))))
         (some
          (fn [prefix-length]
            (and (zero? (mod number-length prefix-length))
                 (let [prefix   (subs number-string 0 prefix-length)
                       repeated (apply str (repeat (/ number-length prefix-length) prefix))]
                   (= repeated number-string)))))
         boolean)))

(defn process-range
  [filter-fn [start end]]
  (->> (range start (inc end))
       (filter filter-fn)
       (apply +)))

(defn day2-part1
  [input]
  (->> input
       parse-input
       (map (partial process-range invalid-part1?))
       (apply +)))

(defn day2-part2
  [input]
  (->> input
       parse-input
       (map (partial process-range invalid-part2?))
       (apply +)))
