package br.com.zup.chave.consulta

import br.com.zup.IdPixConsulta
import io.micronaut.core.annotation.Introspected
import java.lang.IllegalArgumentException

@Introspected
data class NovaConsultaChave(
    val idPixConsulta: IdPixConsulta,
    val chave: String
) {
    var tipoBusca : TipoBusca
    init {
        //Verificando a entrada dos dados
        tipoBusca = if (idPixConsulta.clienteId.isBlank() &&
            idPixConsulta.idPix.isBlank() &&
            chave.isNotBlank()
        ) {
            TipoBusca.BACEN
        }else if (idPixConsulta.clienteId.isNotBlank() &&
            idPixConsulta.idPix.isNotBlank() &&
            chave.isBlank()
        ) {
            TipoBusca.INTERNA
        }else{
            throw IllegalArgumentException("Dados para consulta invalidos.")
        }
    }
}
enum class TipoBusca{
    INTERNA, BACEN
}