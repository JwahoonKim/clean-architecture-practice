package org.example.cleanarcitecturepractice.account.application

import org.example.cleanarcitecturepractice.account.application.port.`in`.GetAccountBalanceUseCase
import org.example.cleanarcitecturepractice.account.application.port.out.LoadAccountPort
import org.example.cleanarcitecturepractice.account.domain.Account
import org.example.cleanarcitecturepractice.account.domain.Money
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class GetAccountBalanceService(
    private val loadAccountPort: LoadAccountPort,
) : GetAccountBalanceUseCase {

    override fun getAccountBalance(accountId: Account.AccountId): Money {
        return loadAccountPort.loadAccount(accountId, LocalDateTime.now())
            .calculateBalance()
    }

}