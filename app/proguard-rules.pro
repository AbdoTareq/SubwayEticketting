# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class cn.pedant.SweetAlert.** { *; }
-dontwarn cn.pedant.SweetAlert.*
-keepnames class cn.pedant.SweetAlert.**

-keep class com.daimajia.** { *; }
           -dontwarn com.daimajia.**
           -keepnames class com.daimajia.**


################ rxjava2 for reactive network library ###########
-dontwarn java.util.concurrent.Flow*
##### END ##############

##ReactNetwork####
-dontwarn com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
-dontwarn io.reactivex.functions.Function
-dontwarn rx.internal.util.**
-dontwarn sun.misc.Unsafe
##END#####
