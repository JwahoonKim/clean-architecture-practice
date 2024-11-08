package org.example.cleanarcitecturepractice.account.application.port.`in`

import org.example.cleanarcitecturepractice.account.domain.Account.AccountId
import org.example.cleanarcitecturepractice.account.domain.Money

interface SendMoneyUseCase {
    fun sendMoney(command: SendMoneyCommand): Boolean
}

data class SendMoneyCommand(
    val sourceAccountId: AccountId,
    val targetAccountId: AccountId,
    val money: Money
) {
    init {
        require(money.isPositive()) { "money must be greater than zero" }
    }
}