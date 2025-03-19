(ns aoc.2023.day18
  (:require
   [clojure.string :as string]
   [clojure.java.io :as io]))

(def input (-> "../input/2023/day18.txt" slurp))
(def sample-input (-> "../input/2023/day18-ex.txt" slurp))

(defn parse-input-part1
  [input]
  (->> input string/split-lines (map #(string/split % #" ")) (map drop-last)))

(def dir-map
  {"0" "R"
   "1" "D"
   "2" "L"
   "3" "U"})

(defn parse-input-part2
  [input]
  (->> input
       string/split-lines
       (map #(string/split % #" "))
       (map last)
       (map #(re-find #"\(#(.{5})(.{1})\)" %))
       (map (fn [[_ qty dir]] [(dir-map dir) (str (Integer/parseInt qty 16))]))))

(defn calculate-points-and-perimeter
  [plans]
  (loop [dig-plans plans
         [cx cy]   [0 0]
         points    [[cx cy]]
         perimeter 0]
    (if-not (seq dig-plans)
      [points perimeter]
      (let [[direction distance] (first dig-plans)
            next-point           (case direction
                                   "R" [cx (+ cy (parse-long distance))]
                                   "L" [cx (- cy (parse-long distance))]
                                   "U" [(- cx (parse-long distance)) cy]
                                   "D" [(+ cx (parse-long distance)) cy])]
        (recur
         (rest dig-plans)
         next-point
         (conj points next-point)
         (+ perimeter (parse-long distance)))))))

(defn shoelace
  [points]
  (/ (->> points
          (partition-all 2 1)
          drop-last
          (map (fn [[[ax ay] [bx by]]] (- (* ax by) (* ay bx))))
          (apply +)
          abs) 2))

;; Uses picks theorem to calculate area
;; https://en.wikipedia.org/wiki/Pick%27s_theorem
;; Formuls
;; A = i + (b / 2) - 1
;; Where i is calculated by shoelace_formula
;; Sum of the cross-products of neighboring vertices.
(defn calculate-distance
  [points perimeter]
  (+ (shoelace points) (/ perimeter 2) 1))


(defn day18-part1
  [input]
  (let [plans              (parse-input-part1 input)
        [points perimeter] (calculate-points-and-perimeter plans)]
    (calculate-distance points perimeter)))

(defn day18-part2
  [input]
  (let [plans               (parse-input-part2 input)
        [points perimeter] (calculate-points-and-perimeter plans)]
    (calculate-distance points perimeter)))

;; Notes
;; Very good explanation by https://www.youtube.com/watch?v=dqwQyrjaHuA&list=PLK9NYUdlGQIQ3UEXZQx-k6cF-t0E4_SNS&index=23
