(ns aoc.2024.day6
  (:require
   [clojure.string :as string]
   [clojure.java.io :as io]))

(def WALL \#)
(def GUARD \^)
(def SPACE \.)

(def NORTH [-1 0])
(def SOUTH [1 0])
(def EAST [0 1])
(def WEST [0 -1])

(def move-right
  {NORTH EAST
   EAST  SOUTH
   SOUTH WEST
   WEST  NORTH})

(defn indexed
  [data]
  (map-indexed (fn [a b] [a b]) data))

(defn process-input
  [input]
  (into {}
        (for [[i row] (indexed input)
              [j ch]  (indexed (apply vector row))]
          [[i j] ch])))

(def sample-input (->> "../input/2024/day6-sample.txt" slurp string/split-lines process-input))
(def input (->> "../input/2024/day6.txt" slurp string/split-lines process-input))

(defn find-guard
  [input]
  (->> input
       (filter (fn [[k v]] (= v GUARD)))
       ffirst))

(defn move-forward
  [position direction]
  (let [[x y] position [dx dy] direction]
    [(+ x dx) (+ y dy)]))

(defn can-move-forward?
  [input position direction]
  (= SPACE (input (move-forward position direction))))

(defn traverse-grid
  [grid start]
  (loop [[position direction] [start NORTH]
         seen #{}]
    (cond
      (can-move-forward? grid position direction)
      (recur [(move-forward position direction) direction] (conj seen position))

      (not (grid (move-forward position direction)))
      (conj seen position)

      :else
      (recur [position (move-right direction)] (conj seen position)))))

(defn day6-part1
  [input]
  (let [start (find-guard input)
        grid  (assoc input start SPACE)]
    (count (traverse-grid grid start))))

(defn loops?
  [grid start]
  (loop [[position direction] [start NORTH]
         seen #{}]
    (if (seen [position direction])
      true
      (cond
        (can-move-forward? grid position direction)
        (recur [(move-forward position direction) direction] (conj seen [position direction]))

        (not (grid (move-forward position direction)))
        false

        :else
        (recur [position (move-right direction)] (conj seen [position direction]))))))

(defn day6-part2
  [input]
  (let [start              (find-guard input)
        grid               (assoc input start SPACE)
        possible-positions (traverse-grid grid start)]
    (reduce (fn [acc position]
              (let [test-grid (assoc grid position WALL)]
                (if (loops? test-grid start) (inc acc) acc)))
            0
            possible-positions)))
