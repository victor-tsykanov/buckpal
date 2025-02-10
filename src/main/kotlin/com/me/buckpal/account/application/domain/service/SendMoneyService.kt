package com.me.buckpal.account.application.domain.service

import com.me.buckpal.account.application.port.`in`.SendMoneyUseCase

class SendMoneyService : SendMoneyUseCase {
    override fun sendMoney(sourceAccountId: String, targetAccountId: String, amount: Long) {
        TODO("Not yet implemented")
    }
}