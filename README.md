High performance block tracking solution for your server.

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
    <version>1.0.6</version>
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
    compileOnly("dev.krakenied:blocktracker:1.0.6")
}
```
Using BlockTracker with LMBishop's [Quests](https://modrinth.com/plugin/quests) plugin
------
##### config.yml
```yml
# PlayerBlockTracker class to be used with the hook
playerblocktracker-class-name: "dev.krakenied.blocktracker.bukkit.BukkitBlockTrackerPlugin"
```
