package com.me.buckpal.account

import com.me.buckpal.account.application.domain.model.Account.AccountId
import com.me.buckpal.account.application.domain.model.Money
import com.me.buckpal.account.application.port.out.LoadAccountPort
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SendMoneySystemTest {
    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var loadAccountPort: LoadAccountPort

    @Test
    @Sql("SendMoneySystemTest.sql")
    fun sendMoney() {
        val response = whenSendMoney(
            sourceAccount().id,
            targetAccount().id,
            Money.of(15),
        )

        then(response.statusCode).isEqualTo(HttpStatus.OK)
        then(sourceAccount().balance).isEqualTo(Money.of(85))
        then(targetAccount().balance).isEqualTo(Money.of(215))
    }

    private fun whenSendMoney(sourceAccountId: AccountId, targetAccountId: AccountId, amount: Money) =
        restTemplate.exchange<Void>(
            "/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}",
            HttpMethod.POST,
            HttpEntity(
                null,
                HttpHeaders().also {
                    it.add(HttpHeaders.CONTENT_TYPE, "application/json")
                },
            ),
            sourceAccountId.value,
            targetAccountId.value,
            amount.longValue(),
        )

    private fun sourceAccount() = loadAccountPort.load(AccountId(1))

    private fun targetAccount() = loadAccountPort.load(AccountId(2))
}