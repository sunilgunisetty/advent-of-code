(ns aoc.2024.day15
  (:require
   [clojure.string :as string]
   [clojure.java.io :as io]))

(def input (-> "../input/2024/day15.txt" slurp))
(def sample-input (-> "../input/2024/day15-ex.txt" slurp))

(def grid-moves
  {">" [0 1]
   "<" [0 -1]
   "^" [-1 0]
   "v" [1 0]})

(defn parse-input
  [input]
  (let [[grid moves] (string/split input #"\n\n")]
    moves))
