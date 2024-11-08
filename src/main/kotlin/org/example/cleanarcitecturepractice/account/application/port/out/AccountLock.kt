package org.example.cleanarcitecturepractice.account.application.port.out

import org.example.cleanarcitecturepractice.account.domain.Account.AccountId


interface AccountLock {
    fun lockAccount(accountId: AccountId)
    fun releaseAccount(accountId: AccountId)
}