package org.example.cleanarcitecturepractice.account.domain

import java.time.LocalDateTime
import org.example.cleanarcitecturepractice.account.domain.Account.AccountId


class ActivityWindow(
    private val activities: List<Activity>
) {

    fun getStartTimeStamp(): LocalDateTime {
        return activities.minBy { it.timestamp }.timestamp
    }

    fun getEndTimeStamp(): LocalDateTime {
        return activities.maxBy { it.timestamp }.timestamp
    }

    fun calculateBalance(accountId: AccountId): Money {
        val depositBalance = activities.filter { a -> a.targetAccountId == accountId }
            .sumOf { it.money.amount }

        val withdrawBalance = activities.filter { a -> a.sourceAccountId == accountId }
            .sumOf { it.money.amount }

        return Money(depositBalance - withdrawBalance)
    }

    fun addActivity(activity: Activity): ActivityWindow {
        return ActivityWindow(activities + activity)
    }
}

