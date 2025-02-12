package com.me.buckpal.account.adapter.out.persistence

import com.me.buckpal.account.application.domain.model.Account
import com.me.buckpal.account.application.port.out.AccountLock
import org.springframework.stereotype.Component

@Component
class NoOpAccountLock : AccountLock {
    override fun lockAccount(accountId: Account.AccountId) {
    }

    override fun releaseAccount(accountId: Account.AccountId) {
    }
}
