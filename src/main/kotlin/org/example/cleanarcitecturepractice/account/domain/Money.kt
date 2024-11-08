package org.example.cleanarcitecturepractice.account.domain

import java.math.BigInteger
import org.springframework.beans.factory.annotation.Value


@JvmInline
value class Money(val amount: BigInteger) {

    fun isPositiveOrZero(): Boolean = amount >= BigInteger.ZERO

    fun isNegativeOrZero(): Boolean = amount <= BigInteger.ZERO

    fun isPositive(): Boolean = amount > BigInteger.ZERO

    fun isGreaterThanOrEqualTo(money: Money): Boolean = amount >= money.amount

    fun isGreaterThan(money: Money): Boolean = amount.compareTo(money.amount) >= 1

    fun minus(money: Money): Money = Money(amount.subtract(money.amount))

    fun plus(money: Money): Money = Money(amount.add(money.amount))

    fun negate(): Money = Money(amount.negate())

    companion object {
        var ZERO: Money = of(0L)

        fun of(value: Long): Money = Money(BigInteger.valueOf(value))

        fun add(a: Money, b: Money): Money = Money(a.amount.add(b.amount))

        fun subtract(a: Money, b: Money): Money = Money(a.amount.subtract(b.amount))
    }
}
