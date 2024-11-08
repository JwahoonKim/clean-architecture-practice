package org.example.cleanarcitecturepractice.account.domain

import java.time.LocalDateTime


class Account private constructor(
    private val id: AccountId,
    private val baselineBalance: Money,
    private val activityWindow: ActivityWindow,
) {

    fun calculateBalance(): Money {
        return Money.add(
            baselineBalance,
            activityWindow.calculateBalance(this.id)
        )
    }

    fun withdraw(money: Money, targetAccountId: AccountId): Boolean {
        if (!mayWithdraw(money)) {
            return false
        }

        val withdrawal = Activity(
            ownerAccountId = this.id,
            sourceAccountId = this.id,
            targetAccountId = targetAccountId,
            timestamp = LocalDateTime.now(),
            money = money
        )

        this.activityWindow.addActivity(withdrawal)
        return true
    }

    private fun mayWithdraw(money: Money): Boolean {
        return Money.subtract(calculateBalance(), money)
            .isPositiveOrZero()
    }

    fun deposit(money: Money, sourceAccountId: AccountId): Boolean {
        val deposit = Activity(
            ownerAccountId = this.id,
            sourceAccountId = sourceAccountId,
            targetAccountId = this.id,
            timestamp = LocalDateTime.now(),
            money = money
        )

        this.activityWindow.addActivity(deposit)
        return true
    }

    companion object {
        fun withoutId(
            baselineBalance: Money,
            activityWindow: ActivityWindow,
        ): Account {
            return Account(AccountId(null), baselineBalance, activityWindow)
        }

        fun withId(
            id: AccountId,
            baselineBalance: Money,
            activityWindow: ActivityWindow,
        ): Account {
            return Account(id, baselineBalance, activityWindow)
        }
    }

    @JvmInline
    value class AccountId(val value: String?)
}