(ns aoc.2020.day5
  (:require
   [clojure.string :as string]
   [clojure.java.io :as io]))

(def input (-> "../input/2020/day5.txt" slurp string/split-lines))
(def sample-input (-> "../input/2020/day5-ex.txt" slurp string/split-lines))

(defn find-row
  [data]
  (let [{:keys [hi lo]}
        (reduce
         (fn [{:keys [lo hi] :as acc} ch]
           (cond
             (= ch \F) (assoc acc :hi (/ (dec (+ lo hi)) 2))
             (= ch \B) (assoc acc :lo (/ (inc (+ lo hi)) 2))))
         {:lo 0 :hi 127}
         data)]
    (assert (= hi lo))
    lo))

(defn find-col
  [data]
  (let [{:keys [hi lo]}
        (reduce
         (fn [{:keys [lo hi] :as acc} ch]
           (cond
             (= ch \L) (assoc acc :hi (/ (dec (+ lo hi)) 2))
             (= ch \R) (assoc acc :lo (/ (inc (+ lo hi)) 2))))
         {:lo 0 :hi 7}
         data)]
    (assert (= hi lo))
    lo))

(defn day5-part1
  [input]
  (->> input
       (map (fn [seat]
              (let [row (find-row (take 7 seat))
                    col (find-col (drop 7 seat))]
                (+ (* row 8) col))))
       (apply max)))

(defn day5-part2
  [input]
  (->> input
       (map (fn [seat]
              (let [row (find-row (take 7 seat))
                    col (find-col (drop 7 seat))]
                (+ (* row 8) col))))
       sort
       (partition-all 2 1)
       drop-last
       (filter (fn [[a b]] (> (- b a) 1)))
       ffirst
       inc))
