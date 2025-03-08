(ns aoc.2024.day14
  (:require
   [clojure.string :as string]
   [clojure.java.io :as io]))

(defn parse-line
  [line]
  (let [[_ x y a b] (re-find #"p=(\d+),(\d+) v=(-?\d+),(-?\d+)" line)]
    [[(parse-long x) (parse-long y)] [(parse-long a) (parse-long b)]]))

(def input (->> "../input/2024/day14.txt" slurp string/split-lines (map parse-line)))
(def sample-input (->> "../input/2024/day14-ex.txt" slurp string/split-lines (map parse-line)))

(defn move
  [x y dx dy wide tall seconds]
  (let [x' (mod (+ x (* dx seconds)) wide)
        y' (mod (+ y (* dy seconds)) tall)]
    [x' y']))

(comment
  ;; below code calculates for every second which is in efficient and ok to solve part1
  (defn robot-position
    [initial-position velocity seconds wide tall]
    (let [[init-x init-y]   initial-position
          [speed-x speed-y] velocity]
      (loop [seconds  seconds
             position [init-x init-y]]
        (if (zero? seconds)
          position
          (recur (dec seconds) (move (first position) (second position) speed-x speed-y wide tall))))))

  (defn calculate-positions
    [seconds wide tall robots]
    (map
     (fn [[initial-position velocity]]
       (robot-position initial-position velocity seconds wide tall)) robots)))

;; More efficient way
;; intuition - moving (+ x dx) x number of times is = (+ d (* dx 100))
;; This optimization helps part 2

(defn calculate-positions
  [seconds wide tall robots]
  (map
   (fn [[[x y] [dx dy]]]
     (move x y dx dy wide tall seconds)) robots))

(defn add-to-quadrant
  [ignore-x ignore-y robots]
  (reduce
   (fn [acc [x y]]
     (cond
       (or (= x ignore-x) (= y ignore-y)) acc
       (and
        (< x ignore-x) (< y ignore-y)) (update acc :top-left #(conj % [x y]))
       (and
        (< x ignore-x) (> y ignore-y)) (update acc :bottom-left #(conj % [x y]))
       (and
        (> x ignore-x) (< y ignore-y)) (update acc :top-right #(conj % [x y]))
       (and
        (> x ignore-x) (> y ignore-y)) (update acc :bottom-right #(conj % [x y]))
       :else
       (assert "invalid case"))
     )
   {:top-left [] :top-right [] :bottom-left [] :bottom-right []}
   robots))

;; used to display the result takes robots final position as set
(defn print-robots
  [robots]
  (doseq [row (map second (sort-by first (group-by first (for [i (range 103) j (range 101)] [i j]))))]
    (prn (string/join "" (map (fn [v] (if (robots v) \# \ )) row)))))

(defn quadrant-count
  [robots-quadrants]
  (reduce-kv (fn [acc k v] (assoc acc k (count v))) {} robots-quadrants))

(defn safty-factor
  [robots-quadrants]
  (->> robots-quadrants vals (map count) (apply *)))

(defn day14-part1
  [input wide tall ignore-x ignore-y seconds]
  (->> input
       (calculate-positions seconds wide tall)
       (add-to-quadrant ignore-x ignore-y)
       safty-factor))

(def part1
  (day14-part1 input 101 103 50 51 100))

(defn day14-part2
  [input wide tall ignore-x ignore-y]
  (ffirst
   (sort-by second
            (for [i (range 1 10000)]
              [i (day14-part1 input wide tall ignore-x ignore-y i)]))))

;; intuition
;; When robots form a tree, some quadrants will have low no of robots and the safety factor will be lower.
(def part2
  (day14-part2 input 101 103 50 51))

(defn print-tree
  [input]
  (->> input
       (calculate-positions 7037 101 103)
       (into #{})
       (print-robots)))

(comment
  "                                                                #                         #          "
  "                                                                                                     "
  "                            #                                                                        "
  "                                                        #                                            "
  "               #           #                                        #    # #                        #"
  "                                                                                                     "
  "                                                                                      #              "
  "                                                                                                     "
  "                   #            #                                                                 #  "
  "                                                                                                     "
  "                                                                        #               #            "
  "                               #         #                          #                                "
  "        #                              #               #   #                                         "
  "                                               #  #                                                  "
  "                                                                                                     "
  "                         #                                                                           "
  "                                    #   #                                                            "
  "       #                     #                                                                       "
  "                                                        #                     #                      "
  "                              #                          #                                           "
  "                          #                                                   #                      "
  "                    #                #################################                               "
  "            #                        #                               #         #                 #   "
  "                               #     #                               #                               "
  "                                     #                               #                               "
  "#                                    #                               #             #                 "
  "                                     #                       #       #                               "
  "                                     #                      ##       #                               "
  "                                     #                  #  ###       #                               "
  " #          #                        #                 ## ####       #                               "
  "                                     #             #  ########       #                               "
  " #                            #      #            ## #########       #    #                          "
  "                      #              #        #  #############       #                               "
  "                            #        #       ## ##############       #                               "
  "                                     #      ##################       #                               "
  "           #      #                  #     ######################    #                               "
  "                                     #    #######################    #                               "
  "             #   #                   #     ######################    #                #              "
  "                                     #      ##################       #                               "
  "                                     #       ## ##############       #                               "
  "                                     #        #  #############       #            #                  "
  "                                     #            ## #########       #                               "
  "                                     #             #  ########       #         #                     "
  "                                     #                 ## ####       #                               "
  " #                                   #                  #  ###       #                               "
  "                                     #                      ##       #                               "
  "                                     #                       #       #                    #          "
  "                  ##           #     #                               #                               "
  "                           #         #                               #                        #      "
  "  #                                  #                               #              #          #     "
  "                                     #                               #                        #      "
  " #                                   #################################                     #         "
  "                 #                                                                         #         "
  "                                                                                                     "
  "                                          #                      #            #                      "
  "                         #                                                 #                         "
  "                                                                                       #        #    "
  "                                                                                                     "
  "                                                          #                                          "
  "                                                                                                   # "
  "    #                                             #                  #                               "
  "                                                                              #                      "
  "#                                                                                                    "
  "                                                    #                                                "
  "       #                                    #                                                        "
  "                                                                                                     "
  "                                                        #                                #           "
  "                       #                            #                                                "
  "     #                                         #                                                     "
  "                     #                                                                               "
  "                                                                                                     "
  "#                  #                                                                                 "
  "                                                                                                     "
  "                     #                              #                        #                       "
  "     #                                                                                  #            "
  "                 #                                                                               #   "
  "                                                                     #             #            #    "
  "                                         #           #  #          #                                 "
  "                                                                                                     "
  "                                                                                                     "
  "    # #                         #                                                                    "
  "                                                                  #                                  "
  "                     #          #                                                                    "
  "           #                                                            #                            "
  "                                       #                                           #                 "
  "                                                                   #                                 "
  "                                                        #                                            "
  "                                                                                                     "
  "#                             #                                                                      "
  "                                                                                                    #"
  "                                                          #                #           #             "
  "                                                 #                                                   "
  "                                                                         #                           "
  "                                          #                                                          "
  "              ##                                                                                #    "
  "#                                                                                                    "
  "                                                                                  #         #        "
  "                                                                                                     "
  "                                                   #                                                 "
  "                                                        #                                            "
  "                                                                                                     "
  "                                                                                                     "
  "                                                                                                     ")
