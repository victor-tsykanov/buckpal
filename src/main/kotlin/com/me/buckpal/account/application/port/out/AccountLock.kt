package com.me.buckpal.account.application.port.out

import com.me.buckpal.account.application.domain.model.Account.AccountId


interface AccountLock {
    fun lockAccount(accountId: AccountId)
    fun releaseAccount(accountId: AccountId)
}
