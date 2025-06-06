package com.me.buckpal.account.application.port.out

import com.me.buckpal.account.application.domain.model.Account
import com.me.buckpal.account.application.domain.model.Account.AccountId

interface LoadAccountPort {
    fun load(id: AccountId): Account
}
