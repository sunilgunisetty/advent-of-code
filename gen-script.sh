#!/usr/bin/env bash

set -eou pipefail

DAY=$1
YEAR=$2

if [ -z "$DAY" ]; then
    echo "Please enter day."
    exit 0
fi

if (("$DAY" > 25 || "$DAY" < 1)); then
    echo "Invalid day $1"
    exit 0
fi

if (("$YEAR" < 2015 || "$YEAR" > $(date +"%Y"))); then
    echo "No AOC for year: $2"
    exit 0
fi

if [[ -f ".aoc_session" ]]; then
  AOCSESSION=$(<".aoc_session")
fi

if [ -z "$AOCSESSION" ]; then
    echo "AOCSESSION missing. Cannot continue."
    exit 0
fi

VALIDSESSION=$(curl -s "https://adventofcode.com/${YEAR}/day/1/input" --cookie "session=${AOCSESSION}")
if [[ $VALIDSESSION =~ "Puzzle inputs differ by user." ]] || [[ $VALIDSESSION =~ "500 Internal Server" ]]; then
    echo "Invalid SESSION. Cannot continue."
    exit 0
fi

DIR="clojure/src/clj/aoc/$YEAR"
FILE="day$DAY.clj"

if [[ ! -d "$DIR" ]]; then
    echo "Directory doesnot exists, creating..."
    mkdir -p $DIR
fi

if [[ ! -f "$DIR/$FILE" ]]; then
    echo "File doesn't exists, creating..."
    touch "$DIR/$FILE"
    echo -n "(ns aoc.${YEAR}.day${DAY}
  (:require
   [clojure.string :as string]
   [clojure.java.io :as io]))" > "$DIR/$FILE"
fi

if [[ -f "input/${YEAR}/day${DAY}.txt" ]]; then
    echo "Script already ran for ${YEAR} - ${DAY}"
    exit 0
fi

if [[ ! -d "input/${YEAR}" ]]; then
    mkdir -p "input/${YEAR}"
fi

curl -s "https://adventofcode.com/${YEAR}/day/${DAY}/input" --cookie "session=${AOCSESSION}" -o "input/${YEAR}/day${DAY}.txt"
