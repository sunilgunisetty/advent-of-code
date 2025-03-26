(ns aoc.2023.day17
  (:require
   [clojure.string :as string]
   [clojure.java.io :as io]
   [clojure.data.priority-map :refer [priority-map]]))

(defn make-grid
  [input]
  (into {}
        (for [[i row] (->> input (map-indexed vector))
              [j ch]  (->> row (map-indexed vector))]
          [[i j] ch])))

(def input (-> "../input/2023/day17.txt" slurp string/split-lines make-grid))
(def sample-input (-> "../input/2023/day17-ex.txt" slurp string/split-lines make-grid))

;; State in priority queue
;; [[x y] :H|:V] Heat
;; :H indicates going East and West
;; :V indicates going North and South
;; Note: Priority hashmap is also used a visited set

(def UP    [-1 0])
(def DOWN  [1 0])
(def LEFT  [0 -1])
(def RIGHT [0 1])

(defn calculate-positions
  [grid [[x y] direction-coordinates] heat steps]
  (let [deltas (for [direction-coordinates [direction-coordinates]
                     n                     (range 1 (inc (last steps)))]
                 (mapv (partial * n) direction-coordinates))]
    (loop [deltas' deltas result [] heat' heat]
      (if-not (seq deltas')
        (drop (dec (first steps)) result)
        (let [[dx dy] (first deltas')
              nx      (+ x dx)
              ny      (+ y dy)]
          (if-not (grid [nx ny])
            (drop (dec (first steps)) result)
            (let [nheat (+ heat' (parse-long (str (grid [nx ny]))))]
              (recur (rest deltas') (conj result [[nx ny] nheat]) nheat))))))))

(defn valid-neighbors
  [grid [[x y] direction] heat steps]
  (let [direction-coordinates (if (= direction :H) [LEFT RIGHT] [UP DOWN])]
    (->> direction-coordinates
         (mapcat (fn [direction-coordinate]
                   (calculate-positions grid [[x y] direction-coordinate] heat steps)))
         (map (fn [[position heat]]
                [[position (if (= direction :H) :V :H)] heat])))))

(defn explore
  [grid steps]
  (let [start [0 0]
        end   (-> grid keys sort last)]
    (loop [queue     (priority-map [start :H] 0 [start :V] 0)
           best-cost {}]
      (let [[[pos direction :as current-node] heat] (first queue)]
        (cond
          (nil? current-node) nil

          (= end pos) heat

          :else
          (let [neighbors
                (->> steps
                     (valid-neighbors grid current-node heat)
                     (filter
                      (fn [[next-node next-heat]]
                        (< next-heat (get queue next-node Long/MAX_VALUE))))
                     (filter
                      (fn [[next-node next-heat]]
                        (< next-heat (best-cost next-node Long/MAX_VALUE)))))]
            (recur
             (into (pop queue) neighbors)
             (assoc best-cost current-node heat))))))))

(defn day17-part1
  [input]
  (explore input [1 2 3]))

(defn day17-part2
  [input]
  (explore input (range 4 11)))


;; NOTE
;; Resources used to solve
;; * https://www.youtube.com/watch?v=8w_rDPdAfBE
;; * https://gitlab.com/maximoburrito/advent2023/-/blob/main/src/day17/main.clj?ref_type=heads
