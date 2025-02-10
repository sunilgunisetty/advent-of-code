(ns aoc.2024.day12
  (:require
   [clojure.string :as string]
   [clojure.java.io :as io]))

(def sample-input (-> "../input/2024/day12-sample.txt" slurp string/split-lines))
(def input (-> "../input/2024/day12.txt" slurp string/split-lines))

(defn indexed
  [data]
  (map-indexed (fn [idx v] [idx v]) data))

(defn parse-input
  [input]
  (into {}
        (for [[x row] (indexed input)
              [y col] (indexed row)]
          [[x y] col])))

(def directions
  [[-1 0] [1 0] [0 1] [0 -1]])

(defn calculate-neighbors
  [[x y]]
  (map (fn [[x' y']] [(+ x x') (+ y y')]) directions))

(defn explore
  [grid visited node]
  (loop [seen  visited
         queue [node]
         res   []]
    (if (seq queue)
      (cond
        (some? (seen (first queue))) (recur seen (vec (rest queue)) res)
        :else
        (let [pos           (first queue)
              current-plant (get grid pos)
              neighbors     (->> pos
                                 calculate-neighbors
                                 (filter grid)
                                 (filter #(= (get grid %) current-plant))
                                 (remove seen))]
          (recur
           (conj seen pos)
           (apply conj (vec (rest queue)) neighbors)
           (conj res pos))))
      [seen res])))

(defn find-connected-components
  [input]
  (let [grid         (parse-input input)
        grid-size    (count input)
        co-ordinates (for [x (range grid-size) y (range grid-size)] [x y])]
    (loop [visited #{}
           nodes co-ordinates
           res []]
      (if (seq nodes)
        (if (visited (first nodes))
          (recur visited (rest nodes) res)
          (let [[new-visited connected-comp] (explore grid visited (first nodes))]
            (recur new-visited (rest nodes) (conj res connected-comp))))
        res))))

(defn calculate-perimeter
  [grid connected-components]
  (reduce
   (fn [acc pos]
     (let [neighbors (calculate-neighbors pos)]
       (+ acc
          (loop [count 0
                 neigh neighbors]
            (if (seq neigh)
              (let [n (first neigh)]
                (cond
                  (nil? (grid n)) (recur (inc count) (rest neigh))
                  (not= (get grid n) (get grid pos)) (recur (inc count) (rest neigh))
                  :else
                  (recur count (rest neigh))))
              count)))))
   0
   connected-components))

(defn day12-part1
  [input]
  (let [grid                 (parse-input input)
        connected-components (find-connected-components input)]
    (reduce
     (fn [acc components]
       (+ acc (* (count components) (calculate-perimeter grid components))))
     0 connected-components)))
