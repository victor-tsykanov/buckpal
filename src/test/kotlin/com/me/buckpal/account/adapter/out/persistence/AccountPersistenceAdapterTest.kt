package com.me.buckpal.account.adapter.out.persistence

import com.me.buckpal.account.application.domain.model.Account
import com.me.buckpal.account.application.domain.model.Account.AccountId
import com.me.buckpal.account.application.domain.model.Money
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.jdbc.Sql

@DataJpaTest
@Import(AccountPersistenceAdapter::class, AccountMapper::class)
class AccountPersistenceAdapterTest {
    @Autowired
    private lateinit var accountPersistenceAdapter: AccountPersistenceAdapter

    @Autowired
    private lateinit var accountRepository: SpringDataAccountRepository

    @Test
    @Sql("AccountPersistenceAdapterTest.sql")
    fun loadAccount() {
        val account = accountPersistenceAdapter.load(AccountId(1))

        assertThat(account.id).isEqualTo(AccountId(1))
        assertThat(account.balance).isEqualTo(Money.of(100))
    }

    @Test
    @Sql("AccountPersistenceAdapterTest.sql")
    fun updateBalance() {
        val account = Account(AccountId(1), Money.of(150))

        accountPersistenceAdapter.updateBalance(account)

        val updatedAccount = accountRepository.findById(1).orElseThrow()
        assertThat(updatedAccount.balance).isEqualTo(150)
    }
}