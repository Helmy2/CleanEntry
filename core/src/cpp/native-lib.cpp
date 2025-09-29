#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring

JNICALL
Java_com_example_clean_entry_core_util_Secrets_getPexelsApiKeyFromNative(
        JNIEnv *env,
        jobject /* this */) {
    std::string apiKey = "ANAcf7Uluv3bBl4FzKZ1uTCZgY4KtsTPBe8TLFPrfvhTXF06C4GsrC43";
    return env->NewStringUTF(apiKey.c_str());
}

extern "C" JNIEXPORT jstring

JNICALL
Java_com_example_clean_entry_core_util_Secrets_getFirebaseApiKeyFromNative(
        JNIEnv *env,
        jobject /* this */) {
    std::string apiKey = "AIzaSyCIoWUnayhp80yTh6QHUgj2EPHhUFybOiM";
    return env->NewStringUTF(apiKey.c_str());
}