package com.me.buckpal.account.application.domain.service

import com.me.buckpal.account.application.port.`in`.SendMoneyCommand
import com.me.buckpal.account.application.port.`in`.SendMoneyUseCase
import com.me.buckpal.account.application.port.out.AccountLock
import com.me.buckpal.account.application.port.out.LoadAccountPort
import com.me.buckpal.account.application.port.out.UpdateAccountStatePort
import com.me.buckpal.common.stereotype.UseCase
import jakarta.transaction.Transactional

@Transactional
@UseCase
class SendMoneyService(
    private val loadAccountPort: LoadAccountPort,
    private val accountLock: AccountLock,
    private val updateAccountStatePort: UpdateAccountStatePort,
) : SendMoneyUseCase {
    override fun sendMoney(command: SendMoneyCommand): Boolean {
        val sourceAccount = loadAccountPort.load(command.sourceAccountId)
        val targetAccount = loadAccountPort.load(command.targetAccountId)

        accountLock.lockAccount(sourceAccount.id)
        if (!sourceAccount.withdraw(command.amount, targetAccount.id)) {
            accountLock.releaseAccount(sourceAccount.id)
            return false
        }

        accountLock.lockAccount(targetAccount.id)
        if (!targetAccount.deposit(command.amount, sourceAccount.id)) {
            accountLock.releaseAccount(sourceAccount.id)
            accountLock.releaseAccount(targetAccount.id)
            return false
        }

        updateAccountStatePort.updateBalance(sourceAccount)
        updateAccountStatePort.updateBalance(targetAccount)

        accountLock.releaseAccount(sourceAccount.id)
        accountLock.releaseAccount(targetAccount.id)

        return true
    }
}
