(ns aoc.2020.day7
  (:require
   [clojure.string :as string]
   [clojure.java.io :as io]))

(def input (-> "../input/2020/day7.txt" slurp string/split-lines))
(def sample-input (-> "../input/2020/day7-ex.txt" slurp string/split-lines))

(defn parse-input
  [input]
  (->> input
       (map (fn [line]
              (let [[[_ _ container] & xs]
                    (re-seq #"(?:^|(\d+) )(\w+ \w+) bags?" line)]
                {container (map (fn [[_ n color]] [(Integer/parseInt n) color]) xs)})))
       (into {})))


(defn color-contains?
  [database container own-color]
  (->> container
       database
       (some
        (fn [[n color]]
          (or
           (= color own-color)
           (color-contains? database color own-color))))))

(defn day7-part1
  [input]
  (let [database (parse-input input)]
    (->> database
         (filter
          (fn [[container _]]
            (color-contains? database container "shiny gold")))
         count)))


(defn count-colors
  [database container]
  (->> container
       database
       (reduce
        (fn [acc [n color]]
          (+ acc n (* n (count-colors database color))))
        0)))

(defn day7-part2
  [input]
  (let [database (parse-input input)]
    (count-colors database "shiny gold")))
