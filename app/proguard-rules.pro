# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# obfuscation for kotlin && reflection classes, see https://medium.com/techwasti/progurad-for-android-kotlin-104a1169fdcd
-dontwarn kotlin.**
-dontwarn kotlin.reflect.jvm.internal.**
-keep class kotlin.reflect.jvm.internal.** { *; }

# exclude parcelable and serializable classes from being renamed, see https://developer.android.com/guide/navigation/navigation-pass-data#proguard_considerations
-keepnames class * extends android.os.Parcelable
-keepnames class * extends java.io.Serializable
-keepclassmembers,allowoptimization class com.mbrainz.sample.** {
    *** Companion;
}
-keepclassmembers,allowoptimization class com.mbrainz.sample.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# hide warnings
-dontwarn java.awt.**
