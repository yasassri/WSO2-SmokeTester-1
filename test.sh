#!/usr/bin/env bash

JMETER_HOME="/home/yasassri/soft/Jmeter/apache-jmeter-3.0"

# Location of the test scripts
scriptlocation="resources/testscripts"
# Report saving location
reporttarget="target/reports"
# Clean the target when running
cleantarget=true

echo "Jmeter Home set to $JMETER_HOME"

#Creating the target directory if not exist
#if [ -d target ]
#then
#    echo "Target Directory already exists! "
#else
#     mkdir target
#     mkdir target/reports
#     mkdir target/logs
#     mkdir target/reports/jtl
#    mkdir target/reports/html
#    mkdir target/javaclasses
#fi

# Cleaning OLD Results and other content
if [ "$cleantarget" = true ]; then
    echo "Cleaning the Target directory"
    rm -rf target
fi

#Creating the target directory
     mkdir target
     mkdir target/reports
     mkdir target/logs
     mkdir target/reports/jtl
    mkdir target/reports/html
    mkdir target/javaclasses

#Copy the report templates to target directory
cp -R resources/template/report/* target/reports/html/

echo "Starting Tests!!"
# Running the Jmeter Scripts
for x in $scriptlocation/*.jmx; do
  echo "Running the Script $x"
  sh $JMETER_HOME/bin/jmeter.sh -n -t $x -l $reporttarget/jtl/test.jtl
  #sh $JMETER_HOME/bin/jmeter.sh -n -t $x -o $reporttarget
done
echo "Test Runs are Over!!"

# Run Report generator Java client
# cp resources/clients/HTMLReportGenerator.java target/javaclasses
echo "Generating the Report!!!"
javac -d target/javaclasses resources/clients/HTMLReportGenerator.java
java -cp target/javaclasses HTMLReportGenerator