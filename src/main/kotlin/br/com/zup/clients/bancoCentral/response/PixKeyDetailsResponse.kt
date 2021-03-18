package br.com.zup.clients.bancoCentral.response

import br.com.zup.TipoConta
import br.com.zup.chave.InstituicoesSTR
import br.com.zup.chave.cadastro.Conta
import br.com.zup.chave.consulta.ChaveInfo
import br.com.zup.clients.bancoCentral.request.BankAccount
import br.com.zup.clients.bancoCentral.request.Owner
import br.com.zup.clients.bancoCentral.request.TipoChaveBacen
import br.com.zup.clients.bancoCentral.request.TipoContaBacen
import java.time.LocalDateTime

data class PixKeyDetailsResponse(
    val keyType: TipoChaveBacen,
    val key: String,
    val bankAccount: BankAccount,
    val owner: Owner,
    val createdAt: LocalDateTime
){
    fun toModel(): ChaveInfo{
        return ChaveInfo(
            tipoChave = keyType.tipoChaveInterno!!,
            chave = key,
            criadoEm = createdAt,
            conta = Conta(
                nomeTitular = owner.name,
                cpfTitular = owner.taxIdNumber,
                instituicao = InstituicoesSTR().getNomeInstituicao(bankAccount.participant),
                agencia = bankAccount.branch,
                numero = bankAccount.accountNumber,
                tipoConta = when(bankAccount.accountType){
                    TipoContaBacen.SVGS -> TipoConta.CONTA_POUPANCA
                    TipoContaBacen.CACC -> TipoConta.CONTA_CORRENTE
                }
            )
        )
    }
}
