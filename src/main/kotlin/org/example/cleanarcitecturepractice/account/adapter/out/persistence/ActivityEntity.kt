package org.example.cleanarcitecturepractice.account.adapter.out.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.example.cleanarcitecturepractice.account.domain.Account
import org.example.cleanarcitecturepractice.account.domain.Account.AccountId
import org.example.cleanarcitecturepractice.account.domain.Activity
import org.example.cleanarcitecturepractice.account.domain.Activity.ActivityId
import org.example.cleanarcitecturepractice.account.domain.ActivityWindow
import org.example.cleanarcitecturepractice.account.domain.Money
import java.time.LocalDateTime



@Entity
class ActivityEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long? = null,

    @Column
    private val timestamp: LocalDateTime? = null,

    @Column
    private val ownerAccountId: Long? = null,

    @Column
    private val sourceAccountId: Long? = null,

    @Column
    private val targetAccountId: Long? = null,

    @Column
    private val amount: Long? = null,
) {
    fun toDomain() : Activity {
        return Activity(
            ownerAccountId = AccountId(id!!),
            sourceAccountId = AccountId(sourceAccountId!!),
            targetAccountId = AccountId(targetAccountId!!),
            timestamp = timestamp!!,
            money = Money.of(amount!!)
        )
    }

    companion object {
        fun fromDomain(activity: Activity): ActivityEntity {
            return ActivityEntity(
                id = activity.id.value,
                timestamp = activity.timestamp,
                ownerAccountId = activity.ownerAccountId.value,
                sourceAccountId = activity.sourceAccountId.value,
                targetAccountId = activity.targetAccountId.value,
                amount = activity.money.amount.toLong()
            )
        }
    }
}