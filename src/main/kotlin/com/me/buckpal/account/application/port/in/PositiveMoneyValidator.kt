package com.me.buckpal.account.application.port.`in`

import com.me.buckpal.account.application.domain.model.Money
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class PositiveMoneyValidator : ConstraintValidator<PositiveMoney, Money> {
    override fun isValid(value: Money, context: ConstraintValidatorContext) = value.isPositive()
}
