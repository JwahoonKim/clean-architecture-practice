package org.example.cleanarcitecturepractice.account.application.port.out

import java.time.LocalDateTime
import org.example.cleanarcitecturepractice.account.domain.Account
import org.example.cleanarcitecturepractice.account.domain.Account.AccountId


interface LoadAccountPort {
    fun loadAccount(accountId: AccountId, baselineDate: LocalDateTime): Account
}