package com.me.buckpal.account.adapter.`in`.web

import com.me.buckpal.account.application.domain.model.Account.AccountId
import com.me.buckpal.account.application.domain.model.Money
import com.me.buckpal.account.application.port.`in`.SendMoneyCommand
import com.me.buckpal.account.application.port.`in`.SendMoneyUseCase
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SendMoneyController(private val sendMoneyUseCase: SendMoneyUseCase) {
    @PostMapping("/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}")
    fun sendMoney(
        @PathVariable sourceAccountId: Long,
        @PathVariable targetAccountId: Long,
        @PathVariable amount: Long,
    ) {
        val sendMoneyCommand = SendMoneyCommand(
            sourceAccountId = AccountId(sourceAccountId),
            targetAccountId = AccountId(targetAccountId),
            amount = Money.of(amount),
        )

        sendMoneyUseCase.sendMoney(sendMoneyCommand)
    }
}
