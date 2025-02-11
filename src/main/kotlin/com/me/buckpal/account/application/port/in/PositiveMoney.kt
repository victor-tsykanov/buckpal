package com.me.buckpal.account.application.port.`in`

import com.me.buckpal.account.application.domain.model.Money
import jakarta.validation.Constraint
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [PositiveMoneyValidator::class])
@MustBeDocumented
annotation class PositiveMoney(
    val message: String = "must be positive, found: \${validatedValue}",
    val groups: Array<KClass<out Any>> = [],
    val payload: Array<KClass<out Money>> = [],
)
