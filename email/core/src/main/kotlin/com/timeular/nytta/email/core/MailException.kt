package com.timeular.nytta.email.core

open class MailException(msg: String): RuntimeException(msg)

class MailConfigurationException(msg: String): MailException(msg)