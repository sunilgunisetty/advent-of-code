(ns aoc.2024.day9
  (:require
   [clojure.string :as string]
   [clojure.java.io :as io]))


(def sample-input (-> "../input/2024/day9-sample.txt" slurp string/trim))
(def input (-> "../input/2024/day9.txt" slurp string/trim))

(defn replace-at [s idx replacement]
  (concat (subvec s 0 idx) replacement (subvec s (inc idx))))

(defn decode
  [disk-map]
  (->> disk-map
       vec
       (partition-all 2)
       (map-indexed (fn [idx v] [idx (vec v)]))
       (mapv (fn [[a [b c]]]
               (concat (take (parse-long (str b)) (repeat a))
                       (take (or (parse-long (str c)) 0) (repeat \.)))))
       (apply concat)
       (into [])))

(defn last-index-of-non-white-space
  [input position]
  (loop [pos position]
    (if (not= (get input pos) \.)
      pos
      (recur (dec pos)))))

(defn first-index-of-white-space
  [input position]
  (loop [pos position]
    (if (= (get input pos) \.)
      pos
      (recur (inc pos)))))

(defn compact
  [decoded-disk-map]
  (loop [ip          decoded-disk-map
         first-index 0
         last-index  (dec (count decoded-disk-map))]
    (let [index-of-free-space     (first-index-of-white-space ip first-index)
          index-of-non-free-space (last-index-of-non-white-space ip last-index)]
      (if-not (<= index-of-non-free-space index-of-free-space)
        (recur
         (-> ip
             (assoc index-of-free-space (get ip index-of-non-free-space))
             (assoc index-of-non-free-space \.))
         (inc index-of-free-space)
         (dec index-of-non-free-space))
        ip))))

(defn day9-part1
  [input]
  (->> input
       decode
       compact
       (take-while #(not= % \.))
       (map-indexed (fn [idx v] (* idx (parse-long (str v)))))
       (apply +)))


(defn update-diskmap
  [data idx val]
  (into [] (concat (subvec data 0 idx) val (subvec data (inc idx)))))

(defn decode-part2
  [disk-map]
  (->> disk-map
       vec
       (partition-all 2)
       (map-indexed (fn [idx v] [idx (vec v)]))
       (map (fn [[idx [ch white-space]]]
              [{:id idx :ch (parse-long (str ch))}
               (or (and white-space {:sp (and white-space (parse-long (str white-space)))})
                   nil)]))
       (apply concat)
       (remove nil?)
       (into [])))

(defn find-space
  [ip size]
  (first (filter (fn [slots] (and (:sp (second slots)) (>= (:sp (second slots)) size))) ip)))

(defn compact-part2
  [decoded-disk-map]
  (loop [first-idx 0
         last-idx  (dec (count decoded-disk-map))
         disk-map  decoded-disk-map]
    (cond
      (< last-idx first-idx)
      disk-map

      (-> disk-map (get last-idx) :sp)
      (recur first-idx (dec last-idx) disk-map)

      :else
      (let [indexed-disk-map      (into [] (map-indexed (fn [idx v] [idx v]) disk-map))
            [idx {:keys [id ch]}] (get indexed-disk-map last-idx)]
        (if-let [[idx {:keys [sp]}] (find-space (take last-idx indexed-disk-map) ch)]
          (let [new-item   (if (zero? (- sp ch))
                             [{:id id :ch ch}]
                             [{:id id :ch ch} {:sp (- sp ch)}])
                updated-ip (-> disk-map
                               (update-diskmap last-idx [{:sp ch}])
                               (update-diskmap idx new-item) )]
            (recur first-idx (dec last-idx) updated-ip))
          (recur first-idx (dec last-idx) disk-map))))))

(defn calculate-checksum-part2
  [compacted-disk-map]
  (loop [current-id 0
         diskmap    compacted-disk-map
         checksum   0]
    (if (seq diskmap)
      (let [data (first diskmap)]
        (if (:sp data)
          (recur (+ current-id (:sp data)) (rest diskmap) checksum)
          (let [{:keys [id ch]} data]
            (recur (+ current-id ch)
                   (rest diskmap)
                   (+ checksum (apply + (map * (range current-id (+ current-id ch)) (repeat id))))))))
      checksum)))

(defn day9-part2
  [input]
  (->> input
       decode-part2
       compact-part2
       calculate-checksum-part2))
