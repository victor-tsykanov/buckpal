package com.me.buckpal.account.application.port.out

import com.me.buckpal.account.application.domain.model.Account

interface UpdateAccountStatePort {
    fun updateBalance(account: Account)
}
