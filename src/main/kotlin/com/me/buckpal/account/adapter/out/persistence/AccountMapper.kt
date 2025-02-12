package com.me.buckpal.account.adapter.out.persistence

import com.me.buckpal.account.application.domain.model.Account
import com.me.buckpal.account.application.domain.model.Account.AccountId
import com.me.buckpal.account.application.domain.model.Money
import org.springframework.stereotype.Component

@Component
class AccountMapper {
    fun mapToDomainEntity(account: AccountJpaEntity) = Account(
        id = AccountId(account.id!!),
        balance = Money.of(account.balance)
    )

    fun mapToJpaEntity(account: Account) = AccountJpaEntity(
        id = account.id.value,
        balance = account.balance.longValue()
    )
}
