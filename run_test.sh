#!/bin/bash

echo "naïf;Welzl récursif;Welzl itératif" > test.csv
for i in {2..1664}
do
  utime1="$( TIMEFORMAT='%3U + %3S';time ( java -jar projet.jar samples/test-"$i".points 0 ) 2>&1 1>/dev/null )"
  utime2="$( TIMEFORMAT='%3U + %3S';time ( java -jar projet.jar samples/test-"$i".points 1 ) 2>&1 1>/dev/null )"
  utime3="$( TIMEFORMAT='%3U + %3S';time ( java -jar projet.jar samples/test-"$i".points 2 ) 2>&1 1>/dev/null )"
  echo "$utime1" | sed 's/,/\./g' | bc -l | tr -d '\n' >> test.csv
  echo -n ";" >> test.csv
  echo "$utime2" | sed 's/,/\./g' | bc -l | tr -d '\n' >> test.csv
  echo -n ";" >> test.csv
  echo "$utime3" | sed 's/,/\./g' | bc -l >> test.csv

done
