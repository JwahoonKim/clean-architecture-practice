package org.example.cleanarcitecturepractice.account.adapter.out.persistence

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.example.cleanarcitecturepractice.account.domain.Account
import org.example.cleanarcitecturepractice.account.domain.ActivityWindow
import org.example.cleanarcitecturepractice.account.domain.Money

@Entity
class AccountEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
) {
    fun toDomain(activityEntities: List<ActivityEntity>, withdrawBalance: Long, depositBalance: Long): Account {
        val baselineBalance = Money.of(withdrawBalance - depositBalance)
        val activities = activityEntities.map { it.toDomain() }

        return Account.withId(
            id = Account.AccountId(id!!),
            baselineBalance = baselineBalance,
            activityWindow = ActivityWindow(activities)
        )
    }
}