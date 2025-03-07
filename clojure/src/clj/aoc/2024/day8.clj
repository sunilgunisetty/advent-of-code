(ns aoc.2024.day8
  (:require
   [clojure.string :as string]
   [clojure.java.io :as io]))

(defn indexed
  [input]
  (map-indexed vector input))

(defn parse-input
  [input]
  (->> (for [[i row] (indexed input)
             [j ch] (indexed (vec row))]
         [[i, j] ch])
       (into {})))

(defn antenna-positions
  [grid]
  (->> grid
       (reduce-kv (fn [acc k v] (if (not= v \.) (assoc acc k v) acc)) {})
       (group-by val)
       (reduce-kv
        (fn [acc k v]
          (assoc acc k (sort-by first (map first v))))
        {})))

(def sample-input (-> "../input/2024/day8-ex.txt" slurp string/split-lines))
(def input (-> "../input/2024/day8.txt" slurp string/split-lines))

(defn antenna-pairs
  [antennas-positions]
  (loop [x antennas-positions acc []]
    (if (seq x)
      (let [a (first x) b (rest x)]
        (recur b (concat acc (mapv (fn [x y] [x y]) (repeat a) b))))
      acc)))

(defn anti-nodes
  [[[x1 y1] [x2 y2]]]
  (let [dx (- x2 x1) dy (- y2 y1)]
    [[(- x1 dx) (- y1 dy)]
     [(+ x2 dx) (+ y2 dy)]]))

(defn find-antinodes
  [grid positions]
  (let [pairs     (antenna-pairs positions)
        antinodes (mapcat anti-nodes pairs)]
    (->> antinodes
         (filter
          (fn [position]
            (and (grid position)))))))

(defn day8-part1
  [input]
  (let [grid     (parse-input input)
        antennas (antenna-positions grid)]
    (->> antennas
         (reduce-kv
          (fn [acc k v]
            (let [antinodes (find-antinodes grid v)]
              (conj acc antinodes)))
          [])
         (apply concat)
         (into #{})
         count)))

;; Simplify this, it looks ugly :(
(defn anti-node-resonate-freq
  [grid-len [[x1 y1] [x2 y2]]]
  (let [dx (- x2 x1) dy (- y2 y1)]
    (concat
     (take-while (fn [[x y]] (and (>= x 0) (>= y 0) (< y grid-len) (< x grid-len))) (iterate (fn [[x y]] [(- x dx) (- y dy)]) [x1 y1]))
     (take-while (fn [[x y]] (and (>= x 0) (>= y 0) (< y grid-len) (< x grid-len))) (iterate (fn [[x y]] [(+ x dx) (+ y dy)]) [x2 y2])))))


(defn find-antinodes-resonate-freq
  [grid-len positions]
  (let [pairs (antenna-pairs positions)]
    (mapcat (fn [v] (anti-node-resonate-freq grid-len v)) pairs)))

(defn day8-part2
  [input]
  (let [antennas (antenna-positions (parse-input input))
        grid-len (count input)]
    (->> antennas
         (reduce-kv
          (fn [acc k v]
            (let [antinodes (find-antinodes-resonate-freq grid-len v)]
              (conj acc antinodes)))
          [])
         (apply concat)
         (into #{})
         count)))
