#!/bin/bash

gcc -c -fPIC -I/usr/lib/jvm/java-18-openjdk-amd64/include -I/usr/lib/jvm/java-18-openjdk-amd64/include/linux nvml-bindings.c -o nvml-bindings.o
gcc -shared -fPIC -o libnvml-bindings.so nvml-bindings.o -lc
mv libnvml-bindings.so ../../libs/