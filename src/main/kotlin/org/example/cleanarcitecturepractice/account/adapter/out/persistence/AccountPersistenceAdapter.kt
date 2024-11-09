package org.example.cleanarcitecturepractice.account.adapter.out.persistence

import org.example.cleanarcitecturepractice.account.application.port.out.LoadAccountPort
import org.example.cleanarcitecturepractice.account.application.port.out.UpdateAccountStatePort
import org.example.cleanarcitecturepractice.account.domain.Account
import org.example.cleanarcitecturepractice.account.domain.Account.AccountId
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class AccountPersistenceAdapter(
    private val accountRepository: AccountRepository,
    private val activityRepository: ActivityRepository,
) : LoadAccountPort, UpdateAccountStatePort{

    override fun loadAccount(accountId: AccountId, baselineDate: LocalDateTime): Account {
        val account = accountRepository.findById(accountId.value!!).orElseThrow()
        val activities = activityRepository.findByOwnerSince(accountId.value, baselineDate)
        val withdrawBalance = activityRepository.getWithdrawalBalanceUntil(accountId.value, baselineDate)
        val depositBalance = activityRepository.getDepositBalanceUntil(accountId.value, baselineDate)
        return account.toDomain(activities, withdrawBalance, depositBalance)
    }

    override fun updateActivities(account: Account) {
        account.activityWindow.activities.forEach { activity ->
            if (activity.id.value == null) {
                activityRepository.save(ActivityEntity.fromDomain(activity))
            }
        }
    }


}