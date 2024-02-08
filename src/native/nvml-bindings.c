#include <jni.h>
#include <nvml.h>
#include <stdio.h>

JNIEXPORT jboolean JNICALL
Java_jni_NVMLBridge_nvmlInit(JNIEnv *env, jobject thiz) {
    nvmlReturn_t result;
    result = nvmlInit();

    return result == NVML_SUCCESS;
}

JNIEXPORT jboolean JNICALL
Java_jni_NVMLBridge_nvmlShutdown(JNIEnv *env, jobject thiz) {
    nvmlReturn_t result;
    result = nvmlShutdown();

    return result == NVML_SUCCESS;
}

JNIEXPORT jint JNICALL
Java_jni_NVMLBridge_nvmlSystemGetCudaDriverVersion(JNIEnv *env, jobject thiz) {
    nvmlReturn_t result;
    int cudaVersion = 0;
    result = nvmlSystemGetCudaDriverVersion(&cudaVersion);
    
    return result == NVML_SUCCESS ? cudaVersion : -1;
}

JNIEXPORT jstring JNICALL
Java_jni_NVMLBridge_nvmlSystemGetDriverVersion(JNIEnv *env, jobject thiz) {
    nvmlReturn_t result;
    char buf[256];
    result = nvmlSystemGetDriverVersion(buf, 256);
    if (result != NVML_SUCCESS) {
        return (*env)->NewStringUTF(env, "nvmlSystemGetDriverVersion failed.");
    }

    return (*env)->NewStringUTF(env, buf);
}

JNIEXPORT jstring JNICALL
Java_jni_NVMLBridge_nvmlSystemGetNVMLVersion(JNIEnv *env, jobject thiz) {
    nvmlReturn_t result;
    char buf[256];
    result = nvmlSystemGetNVMLVersion(buf, 256);
    if (result != NVML_SUCCESS) {
        return (*env)->NewStringUTF(env, "nvmlSystemGetNVMLVersion failed.");
    }

    return (*env)->NewStringUTF(env, buf);
}

JNIEXPORT jlongArray JNICALL
Java_jni_NVMLBridge_nvmlDeviceGetMemoryInfo(JNIEnv *env, jobject thiz) {
    // For now, just grab GPU0. Later on support multiple cards.
    nvmlReturn_t result;
    nvmlDevice_t device;
    result = nvmlDeviceGetHandleByIndex_v2(0, &device);
    if (result != NVML_SUCCESS) {
        printf("Failed to get device 0: %d\n", result);
        return NULL;
    }

    nvmlMemory_t memoryInfo;
    result = nvmlDeviceGetMemoryInfo(device, &memoryInfo);
    if (result != NVML_SUCCESS) {
        printf("Failed to get memory info for device 0: %d\n", result);
        return NULL;
    }

    jlongArray memoryInfoArray = (*env)->NewLongArray(env, 3);
    if (memoryInfoArray == NULL) {
        printf("Failed to allocate array for memory info\n");
        return NULL;
    }

    jlong memoryInfoValues[3] = {
        memoryInfo.free,
        memoryInfo.total,
        memoryInfo.used
    };
    (*env)->SetLongArrayRegion(env, memoryInfoArray, 0, 3, memoryInfoValues);
    return memoryInfoArray;
}

JNIEXPORT jint JNICALL
Java_jni_NVMLBridge_nvmlDeviceGetFanSpeed(JNIEnv *env, jobject thiz) {
    // For now, just grab GPU0. Later on support multiple cards.
    nvmlReturn_t result;
    nvmlDevice_t device;
    result = nvmlDeviceGetHandleByIndex_v2(0, &device);
    if (result != NVML_SUCCESS) {
        printf("Failed to get device 0: %d\n", result);
        return -1;
    }

    unsigned int speed = 0;
    result = nvmlDeviceGetFanSpeed(device, &speed);
    if (result != NVML_SUCCESS) {
        printf("Failed to get fan speed for device 0: %d\n", result);
        return -1;
    }

    return speed;
}

JNIEXPORT jint JNICALL
Java_jni_NVMLBridge_nvmlDeviceGetTemperature(JNIEnv *env, jobject thiz) {
    // For now, just grab GPU0. Later on support multiple cards.
    nvmlReturn_t result;
    nvmlDevice_t device;
    result = nvmlDeviceGetHandleByIndex_v2(0, &device);
    if (result != NVML_SUCCESS) {
        printf("Failed to get device 0: %d\n", result);
        return -1;
    }

    unsigned int temp = 0;
    result = nvmlDeviceGetTemperature(device, NVML_TEMPERATURE_GPU, &temp);
    if (result != NVML_SUCCESS) {
        printf("Failed to get temperature for device 0: %d\n", result);
        return -1;
    }

    return temp;
}

