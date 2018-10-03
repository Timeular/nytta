# Nytta Http Client

The Nytta Http Client is a slim wrapper around [OkHttp http client](https://github.com/square/okhttp).

## Latest release

coming soon.


## Requirements

nytta http client is compiled against JDK8+ and has following required dependencies:

 - okhttp3
 - gson
 - kotson
 - guava
 - slf4j


Additional you can put `spring-retry` on the class path to use the `RetryableHttpClient`.

## Usage

As a base you have an interface called `HttpClient` which has currently two implementations:

* `OkHttpClient`
* `RetryableHttpClient`

In the following example 

### Kotlin

As you can see in following basic example that you only need to instantiate the OkHttpClient
and you are ready to go.

```kotlin
val httpClient: HttpClient = OkHttpClient(timeoutInSeconds = 60)

val response = httpClient.request(
            httpMethod = HttpMethod.GET,
            url = "some url",
            headers = mapOf("Authorization" to "Bearer $accessToken")
            )
            
if( response.wasResponseError() ) {
  // react on the error            
}

val jsonBody = response.body.asJsonObject["data"].asJsonObject
```

If you want to have more complicated URLs (including parameters) you could also use the provided
`UrlBuilder` which also url encodes the parameter values

```kotlin
val url = UrlBuilder.newBuilder()
        .url("$baseUrl/rest/$apiVersion/rest/of/the/uri")
        .addUrlParameter("param1", "value1")
        .addUrlParameter("param2", "value2")
        .addUrlParameter("param3", "value3")
        .build()
```

#### RetryableHttpClient

As an example how to instanziate the `RetryableHttpClient` we will use a annotation based spring 
configuration, because we can also see how you can use it without the spring framework.

**Note:** spring-retry is always required to use that HttpClient.

```kotlin
@Configuration
class HttpClientSpringConfiguration {

    @Bean
    fun okHttpClient(
            @Value("\${timeular.nytta.timeout:60}")
            timeoutInSeconds: Long
    ) = OkHttpClient(timeoutInSeconds = timeoutInSeconds)

    @Bean
    @Primary
    fun retryableHttpClient(
            okHttpClient: OkHttpClient,
            httpRetryTemplate: RetryTemplate
    ): HttpClient = RetryableHttpClient(
            httpClientDelegate = okHttpClient,
            retryTemplate = httpRetryTemplate
    )

    @Bean
    fun httpRetryTemplate(): RetryTemplate {
        val backoffPolicy = ExponentialBackOffPolicy()
        backoffPolicy.initialInterval = 3000
        backoffPolicy.multiplier = 1.75

        val retryTemplate = RetryTemplate()
        retryTemplate.setRetryPolicy(SimpleRetryPolicy(3))
        retryTemplate.setBackOffPolicy(backoffPolicy)

        return retryTemplate
    }
}
```


## License
The nytta http client is released under version 2.0 of the [Apache License][].

[Apache License]: http://www.apache.org/licenses/LICENSE-2.0