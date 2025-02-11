package com.me.buckpal.account.application.port.`in`

import com.me.buckpal.account.application.domain.model.Account.AccountId
import com.me.buckpal.account.application.domain.model.Money
import com.me.buckpal.common.validation.Validation.validate

data class SendMoneyCommand(
    val sourceAccountId: AccountId,
    val targetAccountId: AccountId,
    @PositiveMoney val amount: Money,
) {
    init {
        validate(this)
    }
}
