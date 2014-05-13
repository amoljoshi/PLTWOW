#!/bin/sh
if diff -w -E -b -B "Test/$1/Translated.java" "Test/$1/ExpectedJava.java" >/dev/null ; then
    echo $1 Java test passed!
else
    echo $1 Java test failed!
fi