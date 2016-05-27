# https://docs.fabric.io/android/crashlytics/dex-and-proguard.html

# http://www.crashlytics.com/blog/mastering-proguard-for-building-lightweight-android-code


-keep class com.crashlytics.** { *; }
-keep class com.crashlytics.android.**
-keepattributes SourceFile,LineNumberTable,*Annotation*

# If you are using custom exceptions, add this line so that custom exception types are skipped during obfuscation
-keep public class * extends java.lang.Exception