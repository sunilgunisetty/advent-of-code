(ns aoc.2020.day2
  (:require
   [clojure.string :as string]
   [clojure.java.io :as io]))

(def input (-> "../input/2020/day2.txt" slurp))
(def sample-input (-> "../input/2020/day2-ex.txt" slurp))

(defn parse-input
  [input]
  (->> input
       string/split-lines
       (map #(re-matches #"(\d+)-(\d+) (\w): (\w+)" %))))

(defn day2-part1
  [input]
  (let [data (parse-input input)]
    (count
     (filter
      (fn [[_ min max ch password]]
        (<= (parse-long min)
            (get (frequencies password) (first ch) 0)
            (parse-long max)))
      data))))

(defn day2-part2
  [input]
  (let [data (parse-input input)]
    (count
     (filter
      (fn [[_ fst snd ch password]]
        (let [fst-pos (get password (dec (parse-long fst)))
              snd-pos (get password (dec (parse-long snd)))]
          (not=
           (= (first ch) fst-pos)
           (= (first ch) snd-pos))))
      data))))
