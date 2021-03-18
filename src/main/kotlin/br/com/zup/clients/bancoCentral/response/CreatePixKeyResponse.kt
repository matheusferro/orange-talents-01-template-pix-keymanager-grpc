package br.com.zup.clients.bancoCentral.response

import br.com.zup.clients.bancoCentral.request.BankAccount
import br.com.zup.clients.bancoCentral.request.Owner
import br.com.zup.clients.bancoCentral.request.TipoChaveBacen
import java.time.LocalDateTime

data class CreatePixKeyResponse(
    val keyType: TipoChaveBacen,
    val key: String,
    val bankAccount: BankAccount,
    val owner: Owner,
    val createdAt: LocalDateTime
)