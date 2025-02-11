package com.me.buckpal.account.application.domain.model

class Account(val id: AccountId, private var balance: Money) {
    fun withdraw(amount: Money, targetAccountId: AccountId): Boolean {
        if (!mayWithdraw(amount)) {
            return false
        }

        balance -= amount

        return true
    }

    private fun mayWithdraw(amount: Money) = balance >= amount

    fun deposit(amount: Money, sourceAccountId: AccountId): Boolean {
        balance += amount

        return true
    }

    data class AccountId(val value: Long)
}
