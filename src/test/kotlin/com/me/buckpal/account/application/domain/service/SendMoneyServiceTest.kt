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
        val transferAmount = Money.of(100)

        val sourceAccount = givenSourceAccount()
        val targetAccount = givenTargetAccount()
        givenWithdrawalWillSucceed(sourceAccount)
        givenDepositWillSucceed(targetAccount)

        val success = sendMoneyService.sendMoney(
            SendMoneyCommand(
                sourceAccount.id,
                targetAccount.id,
                transferAmount,
            )
        )

        assertThat(success).isTrue()

        val sourceAccountId = sourceAccount.id
        val targetAccountId = targetAccount.id

        then(accountLock).should().lockAccount(eq(sourceAccountId))
        then(sourceAccount).should().withdraw(eq(transferAmount), eq(targetAccountId))
        then(accountLock).should().releaseAccount(eq(sourceAccountId))

        then(accountLock).should().lockAccount(eq(targetAccountId))
        then(targetAccount).should().deposit(eq(transferAmount), eq(sourceAccountId))
        then(accountLock).should().releaseAccount(eq(targetAccountId))

        then(updateAccountStatePort).should().updateBalance(eq(sourceAccount))
        then(updateAccountStatePort).should().updateBalance(eq(targetAccount))
    }

    @Test
    fun transactionFails() {
        val transferAmount = Money.of(100)

        val sourceAccount = givenSourceAccount()
        val targetAccount = givenTargetAccount()
        givenWithdrawalWillFail(sourceAccount)
        givenDepositWillSucceed(targetAccount)

        val success = sendMoneyService.sendMoney(
            SendMoneyCommand(
                sourceAccount.id,
                targetAccount.id,
                transferAmount,
            )
        )

        assertThat(success).isFalse()

        val sourceAccountId = sourceAccount.id
        val targetAccountId = targetAccount.id

        then(accountLock).should().lockAccount(eq(sourceAccountId))
        then(sourceAccount).should().withdraw(eq(transferAmount), eq(targetAccountId))
        then(accountLock).should().releaseAccount(eq(sourceAccountId))

        then(accountLock).should(times(0)).lockAccount(eq(targetAccountId))

        then(updateAccountStatePort).should(times(0)).updateBalance(eq(sourceAccount))
        then(updateAccountStatePort).should(times(0)).updateBalance(eq(targetAccount))
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

    private fun givenWithdrawalWillFail(account: Account) {
        given(account.withdraw(any<Money>(), any<AccountId>())).willReturn(false)
    }

    private fun givenDepositWillSucceed(account: Account) {
        given(account.deposit(any<Money>(), any<AccountId>())).willReturn(true)
    }
}
