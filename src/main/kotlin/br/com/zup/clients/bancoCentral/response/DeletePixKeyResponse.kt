package br.com.zup.clients.bancoCentral.response

import java.time.LocalDateTime

data class DeletePixKeyResponse(
    val key: String,
    val participant: String,
    val deletedAt : LocalDateTime
)