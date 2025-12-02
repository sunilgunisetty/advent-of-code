(ns aoc.2025.day1
  (:require
   [clojure.string :as string]
   [clojure.java.io :as io]))

(def input (-> "../input/2025/day1.txt" slurp string/split-lines))
(def sample-input (-> "../input/2025/day1-ex.txt" slurp string/split-lines))

(defn parse-input
  [input]
  (->> input
       (map (fn [x]
              {:direction (keyword (string/lower-case (first x)))
               :quantity  (parse-long (apply str (rest x)))}))))

(defn apply-rotation
  [current-pos {:keys [direction quantity]}]
  (if (= direction :l)
    (mod (+ current-pos (- quantity)) 100)
    (mod (+ current-pos quantity) 100)))

(defn day1-part1
  [input]
  (loop [current-position 50
         rotations        (parse-input input)
         password         0]
    (if (seq rotations)
      (let [new-position (apply-rotation current-position (first rotations))]
        (recur new-position (rest rotations) (+ password (if (zero? new-position) 1 0))))
      password)))

(defn counts-clicks
  [current-position {:keys [direction quantity]}]
  (->> (iterate (if (= direction :l) dec inc) current-position)
       (drop 1)
       (take quantity)
       (map #(mod % 100))
       (filter #(= % 0))
       count))

(defn day1-part2
  [input]
  (loop [current-position 50
         rotations        (parse-input input)
         password         0]
    (if (seq rotations)
      (let [new-position (apply-rotation current-position (first rotations))
            no-of-clicks (counts-clicks current-position (first rotations))]
        (recur new-position (rest rotations) (+ password no-of-clicks)))
      password)))
