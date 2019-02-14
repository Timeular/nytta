# Nytta tracking

The Nytta tracking module consists of a server-side tracking integrations (currently only with mixpanel).

## Latest release

Coming soon.

## Usage

Basically, you can configure an instance of `Tracker` which represents an implementation of a tracking provider and use that
instance directly or assign it to a context where you have static access everywhere you want to have it.

### Configuration

#### Plain approach

```java
    TrackerContext.initialize(
            new MixpanelTracker(new OkHttpClient(30), "your-token"),
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
            trackerToken: String,
            @Value("\${tracking.gdpr-token}")
            gpdrToken: String
    ): Tracker {
        val mixpanelTracker = MixpanelTracker(
                token = trackerToken,
                httpClient = OkHttpClient(60),
                gdprToken = gpdrToken
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

````yaml
tracking:
  enabled: ${TRACKING_ENABLED:false}
  token: ${MIX_PANEL_TOKEN:your-token}
  gdpr-token: ${MIX_PANEL_GDPR_TOKEN:your-gdpr-token}
````

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

[Apache License]: http://www.apache.org/licenses/LICENSE-2.0