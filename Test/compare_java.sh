#!/bin/sh
if diff -w "tests/$1/TranslatedJava.java" "tests/$1/ExpectedJava.java" >/dev/null ; then
    echo $1 Java test passed!
else
    echo $1 Java test failed!
fi