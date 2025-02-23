package com.me.buckpal.account

import com.me.buckpal.account.application.domain.model.Account.AccountId
import com.me.buckpal.account.application.domain.model.Money
import com.me.buckpal.account.application.port.out.LoadAccountPort
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.client.RestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SendMoneySystemTest {

    @LocalServerPort
    private var localServerPort: Int = 0

    private val restClient: RestClient
        get() = RestClient.create("http://localhost:$localServerPort")

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
        restClient
            .post()
            .uri(
                "/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}",
                sourceAccountId.value,
                targetAccountId.value,
                amount.longValue(),
            )
            .contentType(MediaType.APPLICATION_JSON)
            .retrieve()
            .toBodilessEntity()

    private fun sourceAccount() = loadAccountPort.load(AccountId(1))

    private fun targetAccount() = loadAccountPort.load(AccountId(2))
}
