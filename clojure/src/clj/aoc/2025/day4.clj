(ns aoc.2025.day4
  (:require
   [clojure.string :as string]
   [clojure.java.io :as io]))

(def input (-> "../input/2025/day4.txt" slurp string/split-lines))
(def sample-input (-> "../input/2025/day4-ex.txt" slurp string/split-lines))

(defn parse-input
  [parsed-input]
  (into {}
        (for [[i row] (map-indexed vector parsed-input)
              [j ch] (map-indexed vector row)]
          [[i j] ch])))

(def directions [[0 1] [0 -1] [1 0] [-1 0] [-1 -1] [-1 1] [1 -1] [1 1]])

(defn find-rolls-positions
  [input-grid]
  (keys (filter (fn [[k v]] (= v \@)) input-grid)))

(defn filter-accessable-roles
  [input-grid rolls-positions]
  (filter
   (fn [[x y]]
     (let [neighbors (map (fn [[dx dy]] [(+ x dx) (+ y dy)]) directions)]
       (< (->> neighbors (map input-grid) (filter #(= % \@)) count) 4)))
   rolls-positions))

(defn day4-part1
  [input]
  (let [input-grid      (parse-input input)
        rolls-positions (find-rolls-positions input-grid)]
    (count
     (filter-accessable-roles input-grid rolls-positions))))

(defn day4-part2
  [input]
  (loop [grid       (parse-input input)
         roll-count 0]
    (let [roll-positions   (find-rolls-positions grid)
          accessible-roles (filter-accessable-roles grid roll-positions)]
      (if (seq accessible-roles)
        (let [new-grid (reduce (fn [acc pos] (assoc acc pos \.)) grid accessible-roles)]
          (recur new-grid (+ roll-count (count accessible-roles))))
        roll-count))))
