(ns aoc.2024.day11
  (:require
   [clojure.string :as string]
   [clojure.java.io :as io]))

(def sample-input (->> "../input/2024/day11-ex.txt" slurp string/trim))
(def input (->> "../input/2024/day11.txt" slurp string/trim))

(defn parse-input
  [input]
  (map parse-long (string/split input #" ")))


(defn blink
  [input times]
  (let [initial-value (reduce (fn [acc v] (assoc acc v 1)) {} input)]
    (loop [count times
           result initial-value]
      (if (zero? count)
        result
        (recur
         (dec count)
         (reduce-kv
          (fn [acc k v]
            (cond
              (zero? k)
              (update acc 1 (fnil #(+ % v) 0))

              (even? (int (+ 1 (Math/floor (Math/log10 k)))))
              (let [len (/ (int (+ 1 (Math/floor (Math/log10 k)))) 2)
                    l (parse-long (apply str (take len (str k))))
                    r (parse-long (apply str (drop len (str k))))]
                (-> acc
                    (update l (fnil #(+ % v) 0))
                    (update r (fnil #(+ % v) 0))))
              :else
              (update acc (* k 2024) (fnil #(+ % v) 0))))
          {}
          result))))))

(defn day11-part1
  [input]
  (let [parsed-input (parse-input input)]
    (reduce-kv (fn [acc _ v] (+ acc v)) 0 (blink parsed-input 25))))

(defn day11-part2
  [input]
  (let [parsed-input (parse-input input)]
    (reduce-kv (fn [acc _ v] (+ acc v)) 0 (blink parsed-input 75))))
