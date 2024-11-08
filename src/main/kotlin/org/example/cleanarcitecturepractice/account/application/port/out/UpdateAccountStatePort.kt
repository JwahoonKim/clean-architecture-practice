package org.example.cleanarcitecturepractice.account.application.port.out

import org.example.cleanarcitecturepractice.account.domain.Account


interface UpdateAccountStatePort {
    fun updateActivities(account: Account)
}