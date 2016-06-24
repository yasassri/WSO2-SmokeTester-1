#!/usr/bin/env bash

scriptlocation="resources/testscripts"
reporttarget="target/reports"
cleantarget=true
JMETER_HOME="/home/yasassri/soft/Jmeter/apache-jmeter-3.0"

echo "Jmeter Home set to $JMETER_HOME"

#Creating the target directory if not exist
if [ -d target ]
then
    echo "Directory already existing"
else
    mkdir target
     mkdir target/reports
    mkdir target/reports/jtl
    mkdir target/reports/html
fi

# Cleanig OLD Results
if [ "$cleantarget" = true ]; then
    echo "Cleaning the Target directory"
    rm -rf $reporttarget/jtl/*
    rm -rf $reporttarget/html/*
fi

# Running the Jmeter Scripts
for x in $scriptlocation/*.jmx; do
  echo "Running the Script $x"
  sh $JMETER_HOME/bin/jmeter.sh -n -t $x -l $reporttarget/jtl/test.jtl -e -o $reporttarget/html
  #sh $JMETER_HOME/bin/jmeter.sh -n -t $x -o $reporttarget
done