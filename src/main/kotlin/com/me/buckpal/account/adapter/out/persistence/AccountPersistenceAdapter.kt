package com.me.buckpal.account.adapter.out.persistence

import com.me.buckpal.account.application.domain.model.Account
import com.me.buckpal.account.application.port.out.LoadAccountPort
import com.me.buckpal.account.application.port.out.UpdateAccountStatePort
import com.me.buckpal.common.stereotype.PersistenceAdapter
import jakarta.persistence.EntityNotFoundException

@PersistenceAdapter
class AccountPersistenceAdapter(
    private val accountRepository: SpringDataAccountRepository,
    private val accountMapper: AccountMapper,
) : LoadAccountPort, UpdateAccountStatePort {
    override fun load(id: Account.AccountId) = accountRepository
        .findById(id.value)
        .orElseThrow(::EntityNotFoundException)
        .let(accountMapper::mapToDomainEntity)

    override fun updateBalance(account: Account) {
        accountMapper
            .mapToJpaEntity(account)
            .also(accountRepository::save)
    }
}
