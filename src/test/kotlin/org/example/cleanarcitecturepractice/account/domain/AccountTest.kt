package org.example.cleanarcitecturepractice.account.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class AccountTest {

    @Test
    fun withdrawTest() {
        val `1만원출금` = Activity(
            ownerAccountId = Account.AccountId(1),
            sourceAccountId = Account.AccountId(1),
            targetAccountId = Account.AccountId(2),
            timestamp = LocalDateTime.now(),
            money = Money.of(10_000L)
        )

        val `2만원입금` = Activity(
            ownerAccountId = Account.AccountId(1),
            sourceAccountId = Account.AccountId(2),
            targetAccountId = Account.AccountId(1),
            timestamp = LocalDateTime.now(),
            money = Money.of(20_000L)
        )

        val activityWindow = ActivityWindow(mutableListOf(`1만원출금`, `2만원입금`))

        val account = Account.withId(
            id = Account.AccountId(1),
            baselineBalance = Money.of(100_000L),
            activityWindow = activityWindow
        )

        val success = account.withdraw(
            Money.of(30_000L),
            Account.AccountId(2)
        )

        assertThat(success).isTrue()
        assertThat(account.calculateBalance()).isEqualTo(Money.of(80_000L))
        assertThat(account.activityWindow.activities.size).isEqualTo(3)
    }
}