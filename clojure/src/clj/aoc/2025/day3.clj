(ns aoc.2025.day3
  (:require
   [clojure.string :as string]
   [clojure.java.io :as io]))

(def input (-> "../input/2025/day3.txt" slurp string/split-lines))
(def sample-input (-> "../input/2025/day3-ex.txt" slurp string/split-lines))

(defn find-joltage
  [battery-bay]
  (->> battery-bay
       seq
       (map (comp parse-long str))
       (apply max)))

(defn process-battery-bay
  [window battery-bay]
  (loop [window-start     -1
         window-positions (range (dec window) -1 -1)
         result           []]
    (if (seq window-positions)
      (let [battery-subset   (subs battery-bay (inc window-start) (- (count battery-bay) (first window-positions)))
            joltage          (find-joltage battery-subset)
            new-window-start (string/index-of battery-bay (str joltage) (inc window-start))]
        (recur new-window-start (rest window-positions) (conj result joltage)))
      (->> result (apply str) parse-long))))

(defn day3-part1
  [input]
  (->> input
       (map (partial process-battery-bay 2))
       (reduce +)))

(defn day3-part2
  [input]
  (->> input
       (map (partial process-battery-bay 12))
       (reduce +)))
