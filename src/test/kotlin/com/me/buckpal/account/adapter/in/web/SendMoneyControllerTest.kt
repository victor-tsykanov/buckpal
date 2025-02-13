package com.me.buckpal.account.adapter.`in`.web

import com.me.buckpal.account.application.domain.model.Account.AccountId
import com.me.buckpal.account.application.domain.model.Money
import com.me.buckpal.account.application.port.`in`.SendMoneyCommand
import com.me.buckpal.account.application.port.`in`.SendMoneyUseCase
import org.junit.jupiter.api.Test
import org.mockito.kotlin.eq
import org.mockito.kotlin.then
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [SendMoneyController::class])
class SendMoneyControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var sendMoneyUseCase: SendMoneyUseCase

    @Test
    fun sendMoney() {
        mockMvc
            .perform(
                post("/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}", 41, 42, 500)
                    .header("Content-Type", "application/json")
            ).andExpectAll(status().isOk)

        then(sendMoneyUseCase)
            .should()
            .sendMoney(
                eq(
                    SendMoneyCommand(
                        AccountId(41),
                        AccountId(42),
                        Money.of(500)
                    )
                )
            )
    }
}
