#include <jni.h>

JNIEXPORT jstring JNICALL
Java_jni_NVMLBridge_getNVMLVersion(JNIEnv *env, jobject thiz) {
	return (*env)->NewStringUTF(env, "Hello from JNI!");
}
