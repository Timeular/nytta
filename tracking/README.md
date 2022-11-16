# Nytta tracking

The Nytta tracking module consists of a server-side tracking integrations (currently only with mixpanel).

## Latest release

The most recent release is tracking 5.4.0, released November 16, 2022.

### Maven

```xml
<dependency>
  <groupId>com.timeular.nytta</groupId>
  <artifactId>tracking</artifactId>
  <version>5.4.0</version>
</dependency>
```

### Gradle

```gradle
// Gradle Groovy DSL
compile 'com.timeular.nytta:tracking:5.4.0'

// Gradle Kotlin DSL
compile(group = "com.timeular.nytta", name = "tracking", version = "5.4.0")
```

### Manual

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.timeular.nytta/tracking/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.timeular.nytta/tracking/badge.svg)
Download the latest release from the maven central repository and add the jar file to you classpath.

## Requirements

nytta tracking is compiled against JDK8+ and has the following required dependencies:

- okhttp3
- gson
- kotson
- slf4j

## Usage

Basically, you can configure an instance of `Tracker` which represents an implementation of a tracking provider and use that
instance directly or assign it to a context where you have static access everywhere you want to have it.

### Configuration

#### Plain approach

```java
    TrackerContext.initialize(
            new MixpanelTracker(new OkHttpClient(), "your-token"),
            true
    );
```

#### Spring

If you want to have the possibility to initialize the tracking instance within the spring context
this is pretty easy to achieve:

```kotlin
@Configuration
class TrackingConfiguration {

    @Bean
    fun mixpanelTracker(
            @Value("\${tracking.enabled}")
            trackingEnabled: Boolean,
            @Value("\${tracking.token}")
            trackerToken: String
    ): Tracker {
        val mixpanelTracker = MixpanelTracker(
                token = trackerToken,
                httpClient = OkHttpClient()
        )

        TrackerContext.initialize(
                tracker = mixpanelTracker,
                enabled = trackingEnabled
        )

        return mixpanelTracker
    }
}
```

As you can see in the example above we are initializing an instance of the `MixpanelTracker` and assign it on the fly
to the `TrackerContext`.

```yaml
tracking:
  enabled: ${TRACKING_ENABLED:false}
  token: ${MIX_PANEL_TOKEN:your-token}
```

### Tracking & updating a profile

For tracking or updating a profile you can call following methods:

```kotlin
// Tracking
TrackerContext.track(userId, "SomeEvent")
TrackerContext.track(userId, "SomeEvent", mapOf("additionalData" to "value"))

// Updating the user profile
TrackerContext.updateProfile(userId, "key", "value")
TrackerContext.updateProfile(userId, mapOf("key" to "value"))
```

## License

The nytta tracking is released under version 2.0 of the [Apache License][].

[apache license]: http://www.apache.org/licenses/LICENSE-2.0
