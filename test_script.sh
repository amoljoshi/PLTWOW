#!/bin/bash
java_count=0

total_java=0

for D in `find Test -type d`
do
    if [ "${D}" != "Test" ]
    then
        name=$(basename ${D})
	
	
	java_test=$(./compare_java.sh $name)
	if [[ "$java_test" == *passed* ]]
	then
	    java_count=`expr $java_count + 1`
	fi
	echo $java_test
	
        total_java=`expr $total_java + 1`
    fi
done
echo "$java_count of $total_java Java tests passed"
