#!/usr/bin/env bash

scriptlocation="resources/testscripts"
reporttarget="target/reports"
cleantarget=true

#Creating the target directory if not exist
if [ -d target ]
then
    echo "Directory already existing"
else
    mkdir target/reports
fi

# Cleanig OLD Results
if [ "$cleantarget" = true ]; then
    echo "Cleaning the Target directory"
    rm -rf $reporttarget/*
fi

# Running the Jmeter Scripts
for x in $scriptlocation/*.jmx; do
  echo "Running the Script $x"
done