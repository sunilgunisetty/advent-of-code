(ns aoc.2025.day6
  (:require
   [clojure.string :as string]
   [clojure.java.io :as io]))

(def input (-> "../input/2025/day6.txt" slurp string/split-lines))
(def sample-input (-> "../input/2025/day6-ex.txt" slurp string/split-lines))

(defn calculate-part1
  [data]
  (apply
   (eval (read-string (string/trim (last data))))
   (map (comp parse-long string/trim) (drop-last data))))

(defn process-input
  [data]
  (loop [rows   data
         idx    0
         result []]
    (let [next-idx (string/index-of (first rows) " " idx)]
      (if next-idx
        (if (every? #(= % \space) (map #(get % next-idx) rows))
          (recur (map #(subs % (inc next-idx)) rows)
                 0
                 (conj result (map #(subs % 0 next-idx) rows)) )
          (recur rows (inc next-idx) result))
        (conj result rows)))))

(defn day6-part1
  [input]
  (->> input
       process-input
       (map calculate-part1)
       (reduce +)))

(defn calculate-part2
  [data]
  (let [op (string/trim (last data))
        ip (map (comp parse-long string/trim) (apply map str (drop-last data)))]
    (apply (eval (read-string op)) ip)))

(defn day6-part2
  [input]
  (->> input
       process-input
       (map calculate-part2)
       (reduce +)))
