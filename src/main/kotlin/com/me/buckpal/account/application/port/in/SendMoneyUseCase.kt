package com.me.buckpal.account.application.port.`in`

interface SendMoneyUseCase {
    fun sendMoney(sourceAccountId: String, targetAccountId: String, amount: Long)
}