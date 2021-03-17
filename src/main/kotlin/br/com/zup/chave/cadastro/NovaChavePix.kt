package br.com.zup.chave.cadastro

import br.com.zup.TipoChave
import br.com.zup.TipoConta
import br.com.zup.chave.ChavePix
import br.com.zup.chave.TipoChaveComValida
import br.com.zup.clients.bancoCentral.request.*
import br.com.zup.validacoes.UUIDValido
import br.com.zup.validacoes.ValidaChave
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * Classe criada para aplicar validacoes
 * e fazer o meio campo entre server e service
 *
 * Utiliza @ValidaChave, para realizar validações da chave
 * baseado no tipo da chave.
 */
@ValidaChave
@Introspected
data class NovaChavePix(

    @field:UUIDValido
    @field:NotBlank
    val clientId: String,

    @field:NotBlank
    val tipoChave: TipoChaveComValida,

    @field:NotNull
    @field:Size(max = 77)
    val chave: String,

    @field:NotBlank
    val tipoConta: TipoConta
) {
    fun toModel(dadosContaItau: Conta, chaveBacen: String): ChavePix {

        return ChavePix(
            this.clientId,
            (TipoChave.valueOf(this.tipoChave.name)),
            (if (this.tipoChave.equals(TipoChaveComValida.ALEATORIA)) chaveBacen else chave),
            dadosContaItau
        )
    }

    fun toRequestBacen(conta: Conta): CreatePixKeyRequest {
        return CreatePixKeyRequest(
            TipoChaveBacen.of(this.tipoChave), //tipo chave [CPF, RANDOM, EMAIL, CNPJ, PHONE]
            this.chave,
            BankAccount(conta.agencia, conta.numero, TipoContaBacen.of(tipoConta)), //tipo conta [CACC, SVGS]
            Owner("NATURAL_PERSON", conta.nomeTitular, conta.cpfTitular) // pessoa [LEGAL_PERSON, NATURAL_PERSON];
        )
    }

}
