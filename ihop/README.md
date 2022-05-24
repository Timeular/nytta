# Nytta ihop

The Nytta ihop consists of various small helping configurations.

## Latest release

The most recent release is ihop 5.3.1, released May 24, 2022.

### Maven

```xml
<dependency>
  <groupId>com.timeular.nytta</groupId>
  <artifactId>ihop</artifactId>
  <version>5.3.1</version>
</dependency>
```

### Gradle

```gradle
// Gradle Groovy DSL
compile 'com.timeular.nytta:ihop:5.3.1'

// Gradle Kotlin DSL
compile(group = "com.timeular.nytta", name = "ihop", version = "5.3.1")
```

### Manual

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.timeular.nytta/ihop/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.timeular.nytta/ihop/badge.svg)
Download the latest release from the maven central repository and add the jar file to you classpath.

## Usage

### UUIDGenerator

This UUIDGenerator contains an interface and a simple implementation using javas `UUID` class
for generating a unique code. The main purpose of this combination is to have a simpler time
when testing UUIDs.

```kotlin
val uuidGenerator : UUIDGenerator = SimpleUUIDGenerator()

uuidGenerator.generateUUID()
```

### NumberUtils

#### toOrdinalString()

Will format an int value to an ordinal string.

```kotlin
// will result in `1st`
println(1.toOrdinalString())

// wil result in `2nd`
println(2.toOrdinalString())
```

### NumberFormatUtils

Simple helper to round numbers

```kotlin
// will result in "0.2"
println(NumberFormatUtils.formatDouble(0.15))

// will result in "1"
println(NumberFormatUtils.formatDouble(1.0))
```

### DateUtils

TBD

### TimezoneUtils

TBD

### MemAnalyzer

Util class to help getting a rough feeling for the memory consumption.

```kotlin
val analyzer = MemAnalyzer()

analyzer.snapshot("start")
...
analyzer.snapshot("savepoint")
...
analyzer.snapshot("end")

println(analyzer.shortSummary())
println(analyzer.detailedSummary())

```

## License

The nytta ihop is released under version 2.0 of the [Apache License][].

[apache license]: http://www.apache.org/licenses/LICENSE-2.0
