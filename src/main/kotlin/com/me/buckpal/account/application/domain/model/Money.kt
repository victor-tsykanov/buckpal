package com.me.buckpal.account.application.domain.model

import java.math.BigInteger


class Money(private val amount: BigInteger) {
    companion object {
        val ZERO = Money.of(0)

        fun of(value: Long): Money {
            return Money(BigInteger.valueOf(value))
        }
    }

    fun isPositive() = amount > BigInteger.ZERO

    fun longValue() = amount.toLong()

    operator fun minus(value: Money) = Money(amount - value.amount)

    operator fun plus(value: Money) = Money(amount + value.amount)

    operator fun compareTo(other: Money) = amount.compareTo(other.amount)
}
