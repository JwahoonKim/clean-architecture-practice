package org.example.cleanarcitecturepractice.account.domain

import java.time.LocalDateTime
import org.example.cleanarcitecturepractice.account.domain.Account.AccountId

class Activity(
    val ownerAccountId: AccountId,
    val sourceAccountId: AccountId,
    val targetAccountId: AccountId,
    val timestamp: LocalDateTime,
    val money: Money
) {

    val id: ActivityId = ActivityId.create()

    @JvmInline
    value class ActivityId(val value: Long?) {
        companion object{
            fun create(): ActivityId {
                return ActivityId(null)
            }
        }
    }
}