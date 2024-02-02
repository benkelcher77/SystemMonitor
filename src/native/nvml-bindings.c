#include <jni.h>
#include <nvml.h>

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
Java_jni_NVMLBridge_nvmlDeviceGetFanSpeed(JNIEnv *env, jobject thiz) {
    // For now, just grab GPU0. Later on support multiple cards.
    nvmlReturn_t result;
    nvmlDevice_t device;
    result = nvmlDeviceGetHandleByIndex_v2(0, &device);
    if (result != NVML_SUCCESS) {
        return -1;
    }

    unsigned int speed = 0;
    result = nvmlDeviceGetFanSpeed(device, &speed);
    if (result != NVML_SUCCESS) {
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
        return -1;
    }

    unsigned int temp = 0;
    result = nvmlDeviceGetTemperature(device, NVML_TEMPERATURE_GPU, &temp);
    if (result != NVML_SUCCESS) {
        return -1;
    }

    return temp;
}

