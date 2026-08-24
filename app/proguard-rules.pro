
-keepattributes *Annotation*
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-keep class ir.persianweb.idmguide.data.model.** { *; }
