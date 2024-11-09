package org.example.cleanarcitecturepractice.account.adapter.out

import org.example.cleanarcitecturepractice.account.application.port.out.AccountLock
import org.example.cleanarcitecturepractice.account.domain.Account
import org.springframework.stereotype.Component

@Component
class NoOpsAccountLock : AccountLock {
    override fun lockAccount(accountId: Account.AccountId) {
        return
    }

    override fun releaseAccount(accountId: Account.AccountId) {
        return
    }
}