# Audroid
Audroid is an Android Library that makes audio recording even easier than it already is. The library
works on Android API 7 and above, and can be easily added to your Android project as a Gradle
dependency.

## Adding Audroid to your project
The library may be referenced by either downloading and importing the library directly, referencing
it as a Maven dependency, or referencing it as a Gradle dependency.

1.Add the JitPack repository to your build file. Add it in your root build.gradle at the end of repositories:
```groovy
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```
2.Add the dependency
```groovy
dependencies {
        compile 'com.github.GoodEnoughSoftware:audroid:v0.2-alpha'
}
```

## Example Usage
