(ns aoc.2020.day6
  (:require
   [clojure.string :as string]
   [clojure.java.io :as io]))

(def input (-> "../input/2020/day6.txt" slurp))
(def sample-input (-> "../input/2020/day6-ex.txt" slurp))

(defn parse-input
  [input]
  (string/split input #"\n\n"))

(defn day6-part1
  [input]
  (let [data (parse-input input)
        xf (comp
            (map #(into #{} %))
            (map #(disj % \newline))
            (map count))]
    (transduce xf + data)))

(defn day6-part2
  [input]
  (let [data (parse-input input)
        xf (comp
            (map string/split-lines)
            (map (fn [group]
                   (apply clojure.set/intersection (map set group))))
            (map count))]
    (transduce xf + data)))
