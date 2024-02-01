#!/bin/bash

gcc -c -fPIC -I"${JAVA_HOME}"/include -I"${JAVA_HOME}"/include/linux nvml-bindings.c -o nvml-bindings.o
gcc -shared -fPIC  -o libnvml-bindings.so nvml-bindings.o -lc -L/usr/lib/x86_64-linux-gnu -lnvidia-ml
mv libnvml-bindings.so ../../libs/