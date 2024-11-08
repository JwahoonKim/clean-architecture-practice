package org.example.cleanarcitecturepractice.account.application

import java.time.LocalDateTime
import org.example.cleanarcitecturepractice.account.application.port.`in`.SendMoneyCommand
import org.example.cleanarcitecturepractice.account.application.port.`in`.SendMoneyUseCase
import org.example.cleanarcitecturepractice.account.application.port.out.AccountLock
import org.example.cleanarcitecturepractice.account.application.port.out.LoadAccountPort
import org.example.cleanarcitecturepractice.account.application.port.out.UpdateAccountStatePort
import org.example.cleanarcitecturepractice.account.domain.Money
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Private

private const val MAX_TRANSFER_THRESHOLD = 1_000_000L

@Transactional
@Service
class SendMoneyService(
    private val loadAccountPort: LoadAccountPort,
    private val updateAccountStatePort: UpdateAccountStatePort,
    private val accountLock: AccountLock,
) : SendMoneyUseCase {

    override fun sendMoney(command: SendMoneyCommand): Boolean {
        checkThreshold(command)

        val baselineDate = LocalDateTime.now().minusDays(10)

        val sourceAccount = loadAccountPort.loadAccount(
            accountId = command.sourceAccountId,
            baselineDate = baselineDate
        )

        val targetAccount = loadAccountPort.loadAccount(
            accountId = command.targetAccountId,
            baselineDate = baselineDate
        )

        val sourceAccountId = sourceAccount.id
        val targetAccountId = targetAccount.id

        accountLock.lockAccount(sourceAccountId)
        if (!sourceAccount.withdraw(command.money, targetAccountId)) {
            accountLock.releaseAccount(sourceAccountId)
            return false
        }

        accountLock.lockAccount(targetAccountId)
        if (!targetAccount.deposit(command.money, sourceAccountId)) {
            accountLock.releaseAccount(sourceAccountId)
            accountLock.releaseAccount(targetAccountId)
            return false
        }

        updateAccountStatePort.updateActivities(sourceAccount)
        updateAccountStatePort.updateActivities(targetAccount)

        accountLock.releaseAccount(sourceAccountId)
        accountLock.releaseAccount(targetAccountId)
        return true
    }

    private fun checkThreshold(command: SendMoneyCommand) {
        if (command.money.isGreaterThan(Money.of(MAX_TRANSFER_THRESHOLD))) {
            throw IllegalArgumentException("max threshold exceeded")
        }
    }

}