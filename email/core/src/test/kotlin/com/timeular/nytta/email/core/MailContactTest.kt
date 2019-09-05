package com.timeular.nytta.email.core

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

internal class MailContactTest{

    @Test
    fun testCreateMailContract(){
        val contact = MailContact("test@me.com")
        assertThat(contact.email, equalTo("test@me.com"))
    }

    @Test
    fun testToString(){
        assertThat(MailContact("test@me.com").toString(), equalTo("test@me.com"))
        assertThat(MailContact("test@me.com", "Hallo du").toString(), equalTo("Hallo du <test@me.com>"))
    }
}