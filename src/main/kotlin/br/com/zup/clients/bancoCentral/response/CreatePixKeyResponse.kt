package br.com.zup.clients.bancoCentral.response

import br.com.zup.clients.bancoCentral.request.BankAccount
import br.com.zup.clients.bancoCentral.request.Owner
import java.time.LocalDateTime

data class CreatePixKeyResponse(
    val keyType: String,
    val key: String,
    val bankAccount: BankAccount,
    val owner: Owner,
    val createdAt: LocalDateTime
)