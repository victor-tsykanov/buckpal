package com.me.buckpal.account.application.domain.model

import com.me.buckpal.account.application.domain.model.Account.AccountId
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.*

class AccountTest {

    @Test
    fun withdrawalSuccess() {
        val account = Account(AccountId(1), Money.of(50))

        val success = account.withdraw(Money.of(15), AccountId(2))

        assertThat(success).isTrue()
        assertThat(account.balance).isEqualTo(Money.of(35))
    }

    @Test
    fun withdrawalFailure() {
        val account = Account(AccountId(1), Money.of(50))

        val success = account.withdraw(Money.of(150), AccountId(2))

        assertThat(success).isFalse()
        assertThat(account.balance).isEqualTo(Money.of(50))
    }

    @Test
    fun depositSuccess() {
        val account = Account(AccountId(1), Money.of(10))

        val success = account.deposit(Money.of(15), AccountId(2))

        assertThat(success).isTrue()
        assertThat(account.balance).isEqualTo(Money.of(25))
    }
}
