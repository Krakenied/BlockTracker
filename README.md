Using BlockTracker in your plugin
------
##### Maven
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```
```xml
<dependency>
    <groupId>dev.krakenied</groupId>
    <artifactId>blocktracker</artifactId>
    <version>1.0.2</version>
</dependency>
```
##### Gradle
```kotlin
repositories {
    maven {
        url = uri("https://jitpack.io/")
    }
}
```
```kotlin
dependencies {
    compileOnly("dev.krakenied:blocktracker:1.0.2")
}
```
