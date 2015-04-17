#!/usr/bin/bash

javac -cp ./EJML.jar; *.java

java -cp ./EJML.jar; SVDKernel > SVDResults.dat
java -cp ./EJML.jar; QRKernel > QRResults.dat
java IncreasingFill > SMMIncreasingFill.dat
java IncreasingSize > SMM IncreasingSize.dat
