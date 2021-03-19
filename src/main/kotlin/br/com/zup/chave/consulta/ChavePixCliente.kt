package br.com.zup.chave.consulta

import br.com.zup.TipoChave
import br.com.zup.TipoConta
import java.time.LocalDateTime
import java.util.*

data class ChavePixCliente (
    val id: UUID,
    val clienteId: String,
    val tipoChave: TipoChave,
    val chave: String,
    val tipoConta: TipoConta,
    val criadoEm: LocalDateTime
)