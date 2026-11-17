# R8 size candidate for the standalone Launcher3 build.
# Add narrowly scoped keep rules only when a concrete reflection or runtime test requires them.

# Owl Launcher: Launcher3 loads many classes by name (fragments, preferences, widgets,
# Dagger-generated factories, protobuf). Keep them so the minified debug build runs.
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod,SourceFile,LineNumberTable
-keep class com.android.launcher3.** { *; }
-keep class com.android.launcher3.**$* { *; }
-keep class androidx.preference.** { *; }
-keep class * extends androidx.fragment.app.Fragment { <init>(); }
-keep class * extends androidx.preference.Preference { <init>(...); }
-keep class * extends com.google.protobuf.GeneratedMessageLite { *; }
-keep class dagger.** { *; }
-dontwarn com.android.launcher3.**
-dontwarn dagger.**
-dontwarn javax.annotation.**
