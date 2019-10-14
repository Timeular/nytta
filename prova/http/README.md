# Nytta prova http

The Nytta prova http module testing helpers which for hamkrest around the [nytta http-client](https://github.com/Timeular/nytta/tree/master/http-client) module.

## Latest release

TBD

## Requirements

nytta prova http project is compiled against JDK8+ and has the following required dependencies:

- nytta http-client
  - okhttp3
  - gson
  - kotson
  - guava
  - slf4j
- hamkrest

## Usage

### Gson

```kotlin
assertThat(jsonArray(), isEmpty)
assertThat(jsonArray(), !isEmpty)
```

### HttpClient

```kotlin

// status codes for 1xx
testWithStatusCode(100, httpContinue)
testWithStatusCode(101, httpSwitchingProtocols)

// status codes for 2xx
for (i in 200..299) {
    testWithStatusCode(i, httpSuccessful)
}

testWithStatusCode(200, httpOk)
testWithStatusCode(201, httpCreated)
testWithStatusCode(204, httpNoContent)

// status code for 3xx
testWithStatusCode(301, httpMovedPermanently)
testWithStatusCode(302, httpMovedTemporarily)
testWithStatusCode(302, httpFound)

// status code for 4xx
for (i in 400..499) {
    testWithStatusCode(i, httpClientError)
}

testWithStatusCode(400, httpBadRequest)
testWithStatusCode(401, httpUnauthorized)
testWithStatusCode(403, httpForbidden)
testWithStatusCode(404, httpNotFound)
testWithStatusCode(405, httpMethodNotAllowed)
testWithStatusCode(409, httpConflict)

// status code for 5xx
for (i in 500..599) {
    testWithStatusCode(i, httpServerError)
}

testWithStatusCode(500, httpInternalServerError)
testWithStatusCode(502, httpBadGateway)
testWithStatusCode(503, httpServiceUnavailable)
testWithStatusCode(504, httpGatewayTimeout)


private fun testWithStatusCode(status: Int, matcher: Matcher<HttpResponse<*>>) {
    assertThat(createHttpResponse(status), matcher)
}

private fun createHttpResponse(status: Int) = TextHttpResponse(
        codeValue = status,
        bodyAsText = null
)
```

## License

The nytta email module is released under version 2.0 of the [Apache License][].

[Apache License]: http://www.apache.org/licenses/LICENSE-2.0
