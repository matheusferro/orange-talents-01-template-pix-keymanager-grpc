package br.com.zup.chave.remocao

import br.com.zup.validacoes.UUIDValido
import io.micronaut.core.annotation.Introspected

@Introspected
data class RemoverChaveRequest(

    @field:UUIDValido
    val idPix: String,

    @field:UUIDValido
    val clienteId: String
)