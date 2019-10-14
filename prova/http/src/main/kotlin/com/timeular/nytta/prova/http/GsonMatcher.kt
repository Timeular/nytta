package com.timeular.nytta.prova.http

import com.google.gson.JsonArray
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.has

val isEmpty = has(JsonArray::size, equalTo(0))