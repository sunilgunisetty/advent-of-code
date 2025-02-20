(ns aoc.2024.day13
  (:require
   [clojure.string :as string]
   [clojure.java.io :as io]))

(def input (-> "../input/2024/day13.txt" slurp))
(def sample-input (-> "../input/2024/day13-sample.txt" slurp))

(defn solve-equation
  [[a1 a2 b1 b2 c1 c2]]
  (let [x (/ (- (* c1 b2) (* b1 c2)) (- (* a1 b2) (* b1 a2)))
        y (/ (- (* a1 c2) (* c1 a2)) (- (* a1 b2) (* b1 a2)))]
    [x y]))

(defn parse-equations
  ([equations]
   (parse-equations equations 1))
  ([equations error-adjustment]
   (let [[eq1 eq2 r] (string/split-lines equations)
         [_ a1 a2] (re-find #"Button A: X\+(\d+), Y\+(\d+)" eq1)
         [_ b1 b2] (re-find #"Button B: X\+(\d+), Y\+(\d+)" eq2)
         [_ c1 c2] (re-find #"Prize: X=(\d+), Y=(\d+)" r)]
     (map parse-long [a1 a2 b1 b2 c1 c2]))))

(defn parse-input
  [input]
  (-> input
      (string/split #"\n\n")))

(defn day13-part1
  [input]
  (->> input
       parse-input
       (map parse-equations)
       (map solve-equation)
       (filter (fn [[x y]] (and (int? x) (int? y))))
       (map (fn [[x y]] (+ (* x 3) y)))
       (apply +)))
