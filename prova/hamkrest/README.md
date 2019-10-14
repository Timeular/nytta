# Nytta prova hamkrest

The Nytta prova hamkrest module provides helpers and services for handling tests in hamkrest.

## Latest release

TBD

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

[Apache License]: http://www.apache.org/licenses/LICENSE-2.0
