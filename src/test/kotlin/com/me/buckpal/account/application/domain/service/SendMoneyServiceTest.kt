package com.me.buckpal.account.application.domain.service

import com.me.buckpal.account.application.domain.model.Account
import com.me.buckpal.account.application.domain.model.Account.AccountId
import com.me.buckpal.account.application.domain.model.Money
import com.me.buckpal.account.application.port.`in`.SendMoneyCommand
import com.me.buckpal.account.application.port.out.AccountLock
import com.me.buckpal.account.application.port.out.LoadAccountPort
import com.me.buckpal.account.application.port.out.UpdateAccountStatePort
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.*
import org.mockito.kotlin.*

class SendMoneyServiceTest {
    private val loadAccountPort = mock<LoadAccountPort>()
    private val accountLock = mock<AccountLock>()
    private val updateAccountStatePort = mock<UpdateAccountStatePort>()
    private val sendMoneyService = SendMoneyService(
        loadAccountPort,
        accountLock,
        updateAccountStatePort,
    )

    @Test
    fun transactionSucceeds() {
        val sourceAccount = givenSourceAccount()
        val targetAccount = givenTargetAccount()

        val success = sendMoneyService.sendMoney(
            SendMoneyCommand(
                sourceAccount.id,
                targetAccount.id,
                Money.of(100),
            )
        )

        assertThat(success).isTrue()
    }

    private fun givenSourceAccount() = givenAccountWithId(AccountId(41))

    private fun givenTargetAccount() = givenAccountWithId(AccountId(42))

    private fun givenAccountWithId(id: AccountId) = mock<Account>().also {
        given(it.id).willReturn(id)
        given(loadAccountPort.load(eq(it.id))).willReturn(it)
    }

    private fun givenWithdrawalWillSucceed(account: Account) {
        given(account.withdraw(any<Money>(), any<AccountId>())).willReturn(true)
    }
}
