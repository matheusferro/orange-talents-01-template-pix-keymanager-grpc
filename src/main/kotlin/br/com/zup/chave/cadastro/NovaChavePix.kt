package br.com.zup.chave.cadastro

import br.com.zup.TipoChave
import br.com.zup.TipoConta
import br.com.zup.chave.ChavePix
import br.com.zup.chave.TipoChaveComValida
import br.com.zup.validacoes.UUIDValido
import br.com.zup.validacoes.ValidaChave
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotBlank
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

    @field:NotBlank
    @field:Size(max = 77)
    val chave: String,

    @field:NotBlank
    val tipoConta: TipoConta
) {
    fun toModel(dadosContaItau: Conta): ChavePix {

        return ChavePix(
            this.clientId,
            (TipoChave.valueOf(this.tipoChave.name)),
            (if (this.tipoChave.equals(TipoChaveComValida.ALEATORIA)) UUID.randomUUID().toString() else chave),
            dadosContaItau
        )
    }

}
