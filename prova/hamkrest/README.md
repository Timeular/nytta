# Nytta prova hamkrest

The Nytta prova hamkrest module provides helpers and services for handling tests in hamkrest.

## Latest release

e most recent release is prova-hamkrest 5.5.0, released October 18, 2023.

### Maven

```xml
<dependency>
  <groupId>com.timeular.nytta.prova</groupId>
  <artifactId>hamkrest</artifactId>
  <version>5.5.0</version>
</dependency>
```

### Gradle

```gradle
// Gradle Groovy DSL
compile 'com.timeular.nytta.prova:hamkrest:5.5.0'

// Gradle Kotlin DSL
compile(group = "com.timeular.nytta.prova", name = "hamkrest", version = "5.5.0")
```

### Manual

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.timeular.nytta.prova/hamkrest/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.timeular.nytta.prova/hamkrest/badge.svg)
Download the latest release from the maven central repository and add the jar file to you classpath.

## Requirements

Nytta prova hamkrest is compiled against JDK8+ and has the following required dependencies:

- hamkrest

## Components

Currently following additional matchers are inside of this module:

- Collection Matchers
  - containsExactly
  - containsInAnyOrder
  - containsDuplicates

### Usage

#### Collection Matchers

```kotlin
assertThat(listOf(1, 2), containsInAnyOrder(1, 2))
assertThat(listOf(1), !containsInAnyOrder(1, 1))
```

##### containsExactly

```kotlin
assertThat(listOf(1, 2), containsExactly(1, 2))
assertThat(listOf(1, 2), !containsExactly(1, 2, 3))
```

##### containsInAnyOrder

##### containsDuplicates

```kotlin
assertThat(listOf(1, 2), !containsDuplicates<Int>())
assertThat(listOf(1, 2, 1, 2), containsDuplicates())
```

## License

The nytta email module is released under version 2.0 of the [Apache License][].

[apache license]: http://www.apache.org/licenses/LICENSE-2.0
