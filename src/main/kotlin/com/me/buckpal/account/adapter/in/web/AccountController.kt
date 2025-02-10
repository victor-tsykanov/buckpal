package com.me.buckpal.account.adapter.`in`.web

import com.me.buckpal.account.application.port.`in`.SendMoneyUseCase
import org.springframework.web.bind.annotation.PostMapping

class AccountController(val sendMoneyUseCase: SendMoneyUseCase) {
    @PostMapping("/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}")
    fun sendMoney() {

    }
}