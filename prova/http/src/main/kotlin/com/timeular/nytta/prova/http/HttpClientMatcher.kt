package com.timeular.nytta.prova.http

import com.natpryce.hamkrest.and
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.greaterThanOrEqualTo
import com.natpryce.hamkrest.has
import com.natpryce.hamkrest.lessThanOrEqualTo
import com.timeular.nytta.http.client.HttpResponse

val httpSuccessful = has(
        HttpResponse<*>::code,
        greaterThanOrEqualTo(200) and lessThanOrEqualTo(299)
)

val httpClientError = has(
        HttpResponse<*>::code,
        greaterThanOrEqualTo(400) and lessThanOrEqualTo(499)
)

val httpServerError = has(
        HttpResponse<*>::code,
        greaterThanOrEqualTo(500) and lessThanOrEqualTo(599)
)

val httpContinue = has(HttpResponse<*>::code, equalTo(100))
val httpSwitchingProtocols = has(HttpResponse<*>::code, equalTo(101))
val httpOk = has(HttpResponse<*>::code, equalTo(200))
val httpCreated = has(HttpResponse<*>::code, equalTo(201))
val httpNoContent = has(HttpResponse<*>::code, equalTo(204))
val httpMovedPermanently = has(HttpResponse<*>::code, equalTo(301))
val httpMovedTemporarily = has(HttpResponse<*>::code, equalTo(302))
val httpFound = httpMovedTemporarily
val httpBadRequest = has(HttpResponse<*>::code, equalTo(400))
val httpUnauthorized = has(HttpResponse<*>::code, equalTo(401))
val httpForbidden = has(HttpResponse<*>::code, equalTo(403))
val httpNotFound = has(HttpResponse<*>::code, equalTo(404))
val httpMethodNotAllowed = has(HttpResponse<*>::code, equalTo(405))
val httpConflict = has(HttpResponse<*>::code, equalTo(409))
val httpInternalServerError = has(HttpResponse<*>::code, equalTo(500))
val httpBadGateway = has(HttpResponse<*>::code, equalTo(502))
val httpServiceUnavailable = has(HttpResponse<*>::code, equalTo(503))
val httpGatewayTimeout = has(HttpResponse<*>::code, equalTo(504))
