(ns aoc.2024.day1
  (:require
   [clojure.string :as string]
   [clojure.java.io :as io]))

(def sample-input (-> "../input/2024/day1-sample.txt" slurp))
(def input (-> "../input/2024/day1.txt" slurp))


(defn day1-part1
  [input]
  (let [parsed-lists (->> input string/split-lines (map #(string/split % #"\s+")))
        first-list   (->> parsed-lists (map (comp #(Integer/parseInt %) first)) sort)
        second-list  (->> parsed-lists (map (comp #(Integer/parseInt %) second)) sort)]
    (apply + (map (fn [a b] (abs (- a b))) first-list second-list))))

(defn day1-part2
  [input]
  (let [parsed-lists     (->> input string/split-lines (map #(string/split % #"\s+")))
        first-list       (->> parsed-lists (map (comp #(Integer/parseInt %) first)))
        second-list-freq (->> parsed-lists
                              (map (comp #(Integer/parseInt %) second))
                              (reduce (fn [acc v] (update acc v (fnil inc 0))) {}))]
    (->>  first-list
          (map
           (fn [lst-element]
             (if-let [freq (get second-list-freq lst-element)]
               (* lst-element freq)
               0)
             ))
          (apply +))))
