package com.me.buckpal.common.validation

import jakarta.validation.ConstraintViolationException
import jakarta.validation.Validation
import jakarta.validation.Validator


object Validation {
    private val validator: Validator = Validation.buildDefaultValidatorFactory().validator

    fun <T> validate(subject: T) {
        val violations = validator.validate(subject)
        if (violations.isNotEmpty()) {
            throw ConstraintViolationException(violations)
        }
    }
}
