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

(def tagged-directions
  [[:up [-1 0]] [:down [1 0]] [:left [0 -1]] [:right [0 1]]])

(defn calculate-neighbors
  [[x y]]
  (map (fn [[x' y']] [(+ x x') (+ y y')]) directions))

(defn explore
  [grid visited node]
  (loop [seen   visited
         queue  [node]
         region []]
    (if (seq queue)
      (cond
        (some? (seen (first queue)))
        (recur seen (vec (rest queue)) region)

        :else
        (let [plant-pos     (first queue)
              current-plant (get grid plant-pos)
              neighbors     (->> plant-pos
                                 calculate-neighbors
                                 (filter grid)
                                 (filter #(= (get grid %) current-plant))
                                 (remove seen))]
          (recur
           (conj seen plant-pos)
           (apply conj (vec (rest queue)) neighbors)
           (conj region plant-pos))))
      [seen region])))

(defn calculate-regions
  [grid grid-size]
  (let [co-ordinates (for [x (range grid-size) y (range grid-size)] [x y])]
    (loop [visited #{}
           nodes co-ordinates
           regions []]
      (if (seq nodes)
        (if (visited (first nodes))
          (recur visited (rest nodes) regions)
          (let [[new-visited region] (explore grid visited (first nodes))]
            (recur new-visited (rest nodes) (conj regions region))))
        regions))))

(defn calculate-perimeter
  [grid region]
  (reduce
   (fn [acc plant-position]
     (let [neighbors (calculate-neighbors plant-position)]
       (+ acc
          (loop [count     0
                 neighbors neighbors]
            (if (seq neighbors)
              (let [neighbor (first neighbors)]
                (cond
                  (nil? (grid neighbor))
                  (recur (inc count) (rest neighbors))

                  (not=
                   (get grid neighbor)
                   (get grid plant-position))
                  (recur (inc count) (rest neighbors))

                  :else
                  (recur count (rest neighbors))))
              count)))))
   0
   region))

(defn day12-part1
  [input]
  (let [grid    (parse-input input)
        regions (calculate-regions grid (count input))]
    (reduce
     (fn [acc region]
       (+ acc (* (count region) (calculate-perimeter grid region))))
     0 regions)))

(defn up-calculate
  [{:keys [up left right]}]
  (reduce
   (fn [acc [x y]]
     (+ acc
        (->> [(some? (left [x y]))
              (some? (right [x y]))
              (and (right [(dec x) (dec y)]) (not (left [x y])))
              (and (left [(dec x) (inc y)]) (not (right [x y])))]
             (filter true?)
             count)))
   0 up))

(defn down-calculate
  [{:keys [down left right]}]
  (reduce
   (fn [acc [x y]]
     (+ acc
        (->> [(some? (left [x y]))
              (some? (right [x y]))
              (and (right [(inc x) (dec y)]) (not (left [x y])))
              (and (left [(inc x) (inc y)]) (not (right [x y])))]
             (filter true?)
             count)))
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
              tagged-directions))))
         initial-state
         region)]
    (+ (up-calculate state) (down-calculate state))))

;; Followed method of counting cornors method detailed in this video https://www.youtube.com/watch?v=iKCgjy7-2nY
(defn day12-part2
  [input]
  (let [grid    (parse-input input)
        regions (calculate-regions grid (count input))]
    (reduce
     (fn [acc region]
       (let [area  (count region)
             sides (calculate-sides (into #{} region))]
         (+ acc (* area sides))))
     0 regions)))




;; Another soultion which

(def cornor-coordinates
  [[[-1 0] [0 -1] [-1 -1]] ;; top left cornor neighbors
   [[-1 0] [0 1] [-1 1]]   ;; top right cornor neighbors
   [[1 0] [0 1] [1 1]]     ;; bottom right cornor neighbors
   [[1 0] [0 -1] [1 -1]]]) ;; bottom left cornor neighbors

(defn filter-cornor-plant-plots
  [grid [x y :as plot]]
  (->> cornor-coordinates
       (map (fn [cornor]
              (map (fn [[x' y']] [(+ x x') (+ y y')]) cornor)))
       (filter
        (fn [[adj1 adj2 opposite]]
          (let [plot-plant     (get grid plot)
                adj1-plant    (get grid adj1)
                adj2-plant    (get grid adj2)
                opposite-plant (get grid opposite)]
            ;; (If both adjecent plants are not equal to current plant)
            ;; OR (if both adjecent plats are equal to current plant AND
            ;; plant is not equal to opposite plant[diagonal]
            ;; Then it contributes to cornor.
            (or
             (and
              (not= plot-plant adj1-plant)
              (not= plot-plant adj2-plant))
             (and
              (= plot-plant adj1-plant)
              (= plot-plant adj2-plant)
              (not= plot-plant opposite-plant))))))))

(defn calculate-sides-another-solution
  [grid region]
  (loop [plant-plots region
         cornors     0]
    (if (seq plant-plots)
      (let [plot         (first plant-plots)
            plot-cornors (count (filter-cornor-plant-plots grid plot))]
        (recur (rest plant-plots) (+ cornors plot-cornors)))
      cornors)))


(defn day12-part2-another-solution
  [input]
  (let [grid    (parse-input input)
        regions (calculate-regions grid (count input))]
    (reduce
     (fn [acc region]
       (let [area (count region)
             sides (calculate-sides-another-solution grid region)]
         (+ acc (* area sides))))
     0 regions)))
