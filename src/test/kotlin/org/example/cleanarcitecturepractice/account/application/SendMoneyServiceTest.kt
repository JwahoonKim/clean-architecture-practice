package org.example.cleanarcitecturepractice.account.application

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.example.cleanarcitecturepractice.account.application.port.`in`.SendMoneyCommand
import org.example.cleanarcitecturepractice.account.application.port.out.AccountLock
import org.example.cleanarcitecturepractice.account.application.port.out.LoadAccountPort
import org.example.cleanarcitecturepractice.account.application.port.out.UpdateAccountStatePort
import org.example.cleanarcitecturepractice.account.domain.Account
import org.example.cleanarcitecturepractice.account.domain.Account.AccountId
import org.example.cleanarcitecturepractice.account.domain.ActivityWindow
import org.example.cleanarcitecturepractice.account.domain.Money
import org.junit.jupiter.api.Test


internal class SendMoneyServiceTest {

    private val loadAccountPort: LoadAccountPort = mockk<LoadAccountPort>()
    private val accountLock: AccountLock = mockk<AccountLock>()
    private val updateAccountStatePort: UpdateAccountStatePort = mockk<UpdateAccountStatePort>()

    private val sendMoneyService = SendMoneyService(
        loadAccountPort = loadAccountPort,
        updateAccountStatePort = updateAccountStatePort,
        accountLock = accountLock
    )

    @Test
    fun `잔액이 모자라면 출금에 실패한다`() {
        val sourceAccount = Account.withId(
            id = AccountId(1L),
            baselineBalance = Money.of(100_000L),
            activityWindow = ActivityWindow(mutableListOf())
        )

        val targetAccount = Account.withId(
            id = AccountId(2L),
            baselineBalance = Money.of(100_000L),
            activityWindow = ActivityWindow(mutableListOf())
        )

        every { loadAccountPort.loadAccount(sourceAccount.id, any()) } returns sourceAccount
        every { loadAccountPort.loadAccount(targetAccount.id, any()) } returns targetAccount
        every { accountLock.lockAccount(any()) } returns Unit
        every { accountLock.releaseAccount(any()) } returns Unit

        val command = SendMoneyCommand(
            sourceAccount.id,
            targetAccount.id,
            Money.of(200_000L)
        )

        val success = sendMoneyService.sendMoney(command)

        assertThat(success).isFalse()

        verify { accountLock.lockAccount(sourceAccount.id) }
        verify { accountLock.releaseAccount(sourceAccount.id) }
        verify(exactly = 0) { accountLock.lockAccount(targetAccount.id) }
    }

    @Test
    fun `잔액이 충분하면 송금에 성공한다`() {
        val sourceAccount = Account.withId(
            id = AccountId(1L),
            baselineBalance = Money.of(200_000L),
            activityWindow = ActivityWindow(mutableListOf())
        )

        val targetAccount = Account.withId(
            id = AccountId(2L),
            baselineBalance = Money.of(100_000L),
            activityWindow = ActivityWindow(mutableListOf())
        )

        every { loadAccountPort.loadAccount(sourceAccount.id, any()) } returns sourceAccount
        every { loadAccountPort.loadAccount(targetAccount.id, any()) } returns targetAccount
        every { accountLock.lockAccount(any()) } returns Unit
        every { accountLock.releaseAccount(any()) } returns Unit
        every { updateAccountStatePort.updateActivities(any()) } returns Unit

        val command = SendMoneyCommand(
            sourceAccount.id,
            targetAccount.id,
            Money.of(50_000L)
        )

        val success = sendMoneyService.sendMoney(command)

        assertThat(success).isTrue()

        verify { accountLock.lockAccount(sourceAccount.id) }
        verify { accountLock.lockAccount(targetAccount.id) }
        verify { accountLock.releaseAccount(sourceAccount.id) }
        verify { accountLock.releaseAccount(targetAccount.id) }
    }
}