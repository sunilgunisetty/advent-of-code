(ns aoc.2024.day10
  (:require
   [clojure.string :as string]
   [clojure.java.io :as io]))

(def sample-input (-> "../input/2024/day10-ex.txt" slurp))
(def input (-> "../input/2024/day10.txt" slurp))

(defn indexed
  ([data]
   (indexed data identity))
  ([data mapping-fn]
   (map-indexed (fn [idx v] [idx (mapping-fn v)]) data)))

(defn parse-input
  [input]
  (into {}
        (for [[i row] (indexed (string/split-lines input))
              [j val] (indexed row (comp parse-long str))]
          [[i j] val])))

(defn trailhead-start
  [parsed-input]
  (->> parsed-input
       (filter (fn [[_ height]] (= height 0)))
       (map first)))

(def north [-1 0])
(def south [1 0])
(def east [0 1])
(def west [0 -1])

(defn neighbors
  [[x y]]
  (map (fn [[x' y']] [(+ x x') (+ y y')]) [north south east west]))

(defn valid-neighbors
  [grid position]
  (let [current-height (grid position)]
    (if (= current-height 9)
      '()
      (->> position
           neighbors
           (filter (fn [p] (= (grid p) (inc current-height))))))))

(defn trailhead-score
  [grid trail-head]
  (loop [seen  #{trail-head}
         stack (list trail-head)
         count 0]
    (cond
      (empty? stack)
      count

      (= (grid (first stack)) 9)
      (recur seen (rest stack) (inc count))

      :else
      (let [stack-top (first stack)
            neighbors (->> stack-top (valid-neighbors grid) (remove (fn [p] (seen p))))]
        (recur
         (apply conj seen neighbors)
         (apply conj (rest stack) neighbors)
         count)))))

(defn day10-part1
  [input]
  (let [grid            (parse-input input)
        start-positions (trailhead-start grid)]
    (->> start-positions
         (map #(trailhead-score grid %))
         (apply +))))

(defn trailhead-rating
  [grid trail-head]
  (loop [stack (list trail-head)
         count 0]
    (cond
      (empty? stack)
      count

      (= (grid (first stack)) 9)
      (recur (rest stack) (inc count))

      :else
      (let [stack-top (first stack)
            neighbors (valid-neighbors grid stack-top)]
        (recur
         (apply conj (rest stack) neighbors)
         count)))))

(defn day10-part2
  [input]
  (let [grid            (parse-input input)
        start-positions (trailhead-start grid)]
    (->> start-positions
         (map #(trailhead-rating grid %))
         (apply +))))
