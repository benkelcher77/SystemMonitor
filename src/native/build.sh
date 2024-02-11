#!/bin/bash

PARENT_DIR="$(dirname -- "$(realpath -- "$0")")"

gcc -c -fPIC -I"${JAVA_HOME}"/include -I"${JAVA_HOME}"/include/linux -I/opt/cuda/targets/x86_64-linux/include "${PARENT_DIR}"/nvml-bindings.c -o "${PARENT_DIR}"/nvml-bindings.o
gcc -shared -fPIC -o "${PARENT_DIR}"/../../libs/libnvml-bindings.so "${PARENT_DIR}"/nvml-bindings.o -lc -lnvidia-ml
