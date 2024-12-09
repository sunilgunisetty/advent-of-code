(ns aoc.2024.day4
  (:require
   [clojure.string :as string]
   [clojure.java.io :as io]))

(def input (->> "../input/2024/day4.txt" slurp string/split-lines))
(def sample-input (->> "../input/2024/day4-sample.txt" slurp string/split-lines))


(defn valid-position?
  [[x y] len]
  (and (>= x 0) (>= y 0) (< x len) (< y len)))

(defn left [x y] [[x y] [x (dec y)] [x (- y 2)] [x (- y 3)]])
(defn right [x y] [[x y] [x (inc y)] [x (+ y 2)] [x (+ y 3)]])
(defn up [x y] [[x y] [(dec x) y] [(- x 2) y] [(- x 3) y]])
(defn down [x y] [[x y] [(inc x) y] [(+ x 2) y] [(+ x 3) y] ])
(defn top-left [x y] [[x y] [(dec x) (dec y)] [(- x 2) (- y 2)] [(- x 3) (- y 3)]])
(defn bottom-left [x y] [[x y] [(inc x) (dec y)] [(+ x 2) (- y 2)] [(+ x 3) (- y 3)]])
(defn top-right [x y] [[x y] [(dec x) (inc y)] [(- x 2) (+ y 2)] [(- x 3) (+ y 3)]])
(defn bottom-right [x y] [[x y] [(inc x) (inc y)] [(+ x 2) (+ y 2)] [(+ x 3) (+ y 3)]])

(defn x-positions-part-1
  [x y]
  (map #(% x y) [left right up down top-left top-right bottom-left bottom-right]))

(defn filter-valid-positions
  [len positions]
  (filter (fn [position] (every? #(valid-position? % len) position)) positions))

(defn all-index-of
  [word ch]
  (loop [pos -1 res []]
    (if-let [idx (string/index-of word ch pos)]
      (recur (inc idx) (conj res idx))
      res)))

(defn char-positions
  [ch input len]
  (->> input
       (map-indexed
        (fn [idx v]
          (map (fn [a b] [a b])
               (repeat idx)
               (all-index-of v ch))))
       (apply concat [])))

(defn x-check-part-1
  [input [a b c d]]
  (and
   (= (get-in input a) \X)
   (= (get-in input b) \M)
   (= (get-in input c) \A)
   (= (get-in input d) \S)))

(defn day4-part1
  [input]
  (let [len       (count input)
        positions (char-positions "X" input len)]
    (reduce
     (fn [acc [x y]]
       (+ acc (->> (x-positions-part-1 x y)
                   (filter-valid-positions len)
                   (map (fn [v] (if (x-check-part-1 input v) 1 0)))
                   (apply +))))
     0 positions)))


(defn x-positions-part-2
  [x y]
  [[(inc x) (dec y)][x y] [(dec x) (inc y)]
   [(dec x) (dec y)][x y] [(inc x) (inc y)]])


(defn x-check-part-2
  [input [a b c d e f]]
  (or
   (and
    (= (get-in input a) \M)
    (= (get-in input b) \A)
    (= (get-in input c) \S)
    (= (get-in input d) \M)
    (= (get-in input e) \A)
    (= (get-in input f) \S))
   (and
    (= (get-in input a) \M)
    (= (get-in input b) \A)
    (= (get-in input c) \S)
    (= (get-in input d) \S)
    (= (get-in input e) \A)
    (= (get-in input f) \M))
   (and
    (= (get-in input a) \S)
    (= (get-in input b) \A)
    (= (get-in input c) \M)
    (= (get-in input d) \S)
    (= (get-in input e) \A)
    (= (get-in input f) \M))
   (and
    (= (get-in input a) \S)
    (= (get-in input b) \A)
    (= (get-in input c) \M)
    (= (get-in input d) \M)
    (= (get-in input e) \A)
    (= (get-in input f) \S))))

(defn day4-part2
  [input]
  (let [len       (count input)
        positions (char-positions "A" input len)]
    (reduce
     (fn [acc [x y]]
       (let [poss (x-positions-part-2 x y)]
         (if (and (every? #(valid-position? % len) poss) (x-check-part-2 input poss))
           (inc acc)
           acc)))
     0 positions)))
