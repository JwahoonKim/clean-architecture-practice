package org.example.cleanarcitecturepractice.account.application.port.`in`

import org.example.cleanarcitecturepractice.account.domain.Account.AccountId
import org.example.cleanarcitecturepractice.account.domain.Money

interface GetAccountBalanceUseCase {
    fun getAccountBalance(accountId: AccountId): Money
}