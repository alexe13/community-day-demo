package ru.yandex.demo.common

import java.lang.RuntimeException

class TransitionNotAllowedException(message: String): RuntimeException(message)
