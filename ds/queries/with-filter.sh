#!/bin/bash

path='singleCourseQueries'
files=`ls $path`
echo $files

if [ ! -d gtQueries ]
then
    mkdir gtQueries
fi


for f in $files
do
    sed 's/\t?g ?p ?o/\t?g univ:grade_value ?v.\n\t?g ?p ?o.\n\tFILTER (?v > "6")/' "${path}/$f" > "gtQueries/$f"
done