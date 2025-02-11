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

(def directions-positions
  [[:up [-1 0]] [:down [1 0]] [:left [0 -1]] [:right [0 1]]])

(defn up-calculate
  [{:keys [up left right]}]
  (reduce
   (fn [acc [x y]]
     (let [u [(some? (left [x y]))
              (some? (right [x y]))
              (and (right [(dec x) (dec y)]) (not (left [x y])))
              (and (left [(dec x) (inc y)]) (not (right [x y])))]]
       (+ acc
          (count
           (filter true? u)))))
   0 up))

(defn down-calculate
  [{:keys [down left right]}]
  (reduce
   (fn [acc [x y]]
     (let [u [(some? (left [x y]))
              (some? (right [x y]))
              (and (right [(inc x) (dec y)]) (not (left [x y])))
              (and (left [(inc x) (inc y)]) (not (right [x y])))]]
       (+ acc
          (count
           (filter true? u)))))
   0 down))

(defn calculate-sides
  [region]
  (let [initial-state {:up #{} :down #{} :left #{} :right #{}}
        state
        (reduce
         (fn [acc [x y]]
           (reduce
            (fn [acc [pos _]]
              (update acc pos #(conj % [x y])))
            acc
            (filter
             (fn [[_ truthy]] truthy)
             (map
              (fn [[pos [x' y']]]
                [pos (not (region [(+ x x') (+ y y')]))])
              directions-positions))))
         initial-state
         region)]
    (+ (up-calculate state) (down-calculate state))))

(defn day12-part1
  [input]
  (let [grid                 (parse-input input)
        connected-components (find-connected-components input)]
    (reduce
     (fn [acc components]
       (+ acc (* (count components) (calculate-perimeter grid components))))
     0 connected-components)))

;; Followed method of counting cornors method detailed in this https://www.youtube.com/watch?v=iKCgjy7-2nY
(defn day12-part2
  [input]
  (let [connected-components (find-connected-components input)]
    (reduce
     (fn [acc component]
       (let [sides-cost (calculate-sides (into #{} component))]
         (+ acc (* (count component) sides-cost))))
     0 connected-components)))


;; TODO needs refactoring ... :(
