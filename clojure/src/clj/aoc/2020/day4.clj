(ns aoc.2020.day4
  (:require
   [clojure.string :as string]
   [clojure.java.io :as io]))

(def input (-> "../input/2020/day4.txt" slurp))
(def sample-input (-> "../input/2020/day4-ex.txt" slurp))

(defn parse-passport
  [passport]
  (->> passport
       (map #(string/split % #":"))
       (reduce (fn [acc [k v]] (assoc acc (keyword k) v)) {})))

(defn parse-input
  [input]
  (->> (string/split input #"\n\n")
       (map string/split-lines)
       (map #(string/join " " %))
       (map #(string/split % #" "))
       (map parse-passport)))

(def validators
  {:byr (fn [v] (<= 1920 (parse-long v) 2002))
   :iyr (fn [v] (<= 2010 (parse-long v) 2020))
   :eyr (fn [v] (<= 2020 (parse-long v) 2030))
   :hgt (fn [v]
          (let [[_ h unit] (re-matches #"(\d+)(cm|in)" v)]
            (cond
              (= unit "cm") (<= 150 (parse-long h) 193)
              (= unit "in") (<= 59 (parse-long h) 76)
              :else false)))
   :hcl (fn [v] (some? (re-matches #"#[a-f0-9]{6}" v)))
   :ecl (fn [v] (#{"amb" "blu" "brn" "gry" "grn" "hzl" "oth"} v))
   :pid (fn [v] (some? (re-matches #"[0-9]{9}" v)))
   :cid (fn [v] true)})

(defn day4-part1
  [input]
  (let [passports (parse-input input)]
    (->> passports
         (filter
          (fn [{:keys [byr iyr eyr hgt hcl ecl pid]}]
            (and byr iyr eyr hgt hcl ecl pid)))
         count)))

(defn validate
  [passport]
  (reduce-kv
   (fn [acc k v]
     (and acc ((validators k) v)))
   true
   passport))

(defn day4-part2
  [input]
  (let [passports (parse-input input)]
    (->> passports
         (filter
          (fn [{:keys [byr iyr eyr hgt hcl ecl pid] :as passport}]
            (and byr iyr eyr hgt hcl ecl pid (validate passport))))
         count)))
