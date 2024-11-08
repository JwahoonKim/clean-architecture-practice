package org.example.cleanarcitecturepractice.account.adapter.`in`.web

import org.example.cleanarcitecturepractice.account.application.port.`in`.SendMoneyCommand
import org.example.cleanarcitecturepractice.account.application.port.`in`.SendMoneyUseCase
import org.example.cleanarcitecturepractice.account.domain.Account
import org.example.cleanarcitecturepractice.account.domain.Account.AccountId
import org.example.cleanarcitecturepractice.account.domain.Money
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SendMoneyController(
    private val sendMoneyUseCase: SendMoneyUseCase,
) {

    @PostMapping("/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}")
    fun sendMoney(
        sourceAccountId: Long,
        targetAccountId: Long,
        amount: Long,
    ) {
        val command = SendMoneyCommand(
            sourceAccountId = AccountId(sourceAccountId),
            targetAccountId = AccountId(targetAccountId),
            money = Money.of(amount)
        )

        sendMoneyUseCase.sendMoney(command)
    }

}