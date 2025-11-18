(ns aoc.2022.day1
  (:require
   [clojure.string :as string]
   [clojure.java.io :as io]))

(def input (-> "../input/2022/day1.txt" slurp))
(def sample-input (-> "../input/2022/day1-ex.txt" slurp))