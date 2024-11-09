package org.example.cleanarcitecturepractice.account.adapter.out.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface AccountRepository : JpaRepository<AccountEntity, Long>