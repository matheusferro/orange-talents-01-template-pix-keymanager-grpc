package br.com.zup.chave.consulta

import br.com.zup.chave.ChavePix
import br.com.zup.chave.TipoChaveComValida
import br.com.zup.chave.cadastro.Conta
import java.time.LocalDateTime
import java.util.*

data class ChaveInfo (
    val idPix: UUID? = null,
    val clienteId: UUID? = null,
    val tipoChave: TipoChaveComValida,
    val chave: String,
    val criadoEm: LocalDateTime,
    val conta: Conta
){
    companion object{
        fun of(chavePix: ChavePix): ChaveInfo{
            return ChaveInfo(
                chavePix.id,
                UUID.fromString(chavePix.clienteId),
                TipoChaveComValida.valueOf(chavePix.tipoChave.name),
                chavePix.chave,
                chavePix.criadoEm,
                chavePix.conta
            )
        }
    }
}