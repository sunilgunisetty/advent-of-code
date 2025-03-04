#!/usr/bin/env bash

set -eou pipefail

day=$1
year=$2

if [ -z "$day" ]; then
    echo "Please enter day."
    exit 0
fi

if (("$day" > 25 || "$day" < 1)); then
    echo "Invalid day $1"
    exit 0
fi

if (("$year" < 2015 || "$year" > $(date +"%Y"))); then
    echo "No AOC for year: $2"
    exit 0
fi

if [[ -f ".aoc_session" ]]; then
  aoc_session=$(<".aoc_session")
fi

if [ -z "$aoc_session" ]; then
    echo "Session missing. Cannot continue."
    exit 0
fi

valid_session=$(curl -s "https://adventofcode.com/${year}/day/1/input" --cookie "session=${aoc_session}")
if [[ $valid_session =~ "Puzzle inputs differ by user." ]] || [[ $valid_session =~ "500 Internal Server" ]]; then
    echo "Invalid SESSION. Cannot continue."
    exit 0
fi

dir="clojure/src/clj/aoc/${year}"
file="day${day}.clj"

if [[ ! -d "$dir" ]]; then
    echo "Directory doesnot exists, creating..."
    mkdir -p $dir
fi

if [[ ! -f "${dir}/${file}" ]]; then
    echo "File doesn't exists, creating..."
    touch "${dir}/${file}"
    echo -n "(ns aoc.${year}.day${day}
  (:require
   [clojure.string :as string]
   [clojure.java.io :as io]))" > "${dir}/${file}"
fi

if [[ -f "input/${year}/day${day}.txt" ]]; then
    echo "Script already ran for ${year} - ${day}"
    exit 0
fi

if [[ ! -d "input/${year}" ]]; then
    mkdir -p "input/${year}"
fi

curl -s "https://adventofcode.com/${year}/day/${day}/input" --cookie "session=${aoc_session}" -o "input/${year}/day${day}.txt"
