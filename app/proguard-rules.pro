# Keep Kotlin metadata
-keepclassmembers class kotlin.Metadata {
    *** d();
}

# Keep Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.stream.** { *; }
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# Keep model classes
-keep class com.example.intelligentassistant.model.** { *; }
-keep class com.example.intelligentassistant.data.** { *; }

# Keep our app classes
-keep class com.example.intelligentassistant.ui.** { *; }
-keep class com.example.intelligentassistant.service.** { *; }

# Accessibility service
-keep class com.example.intelligentassistant.accessibility.** { *; }
