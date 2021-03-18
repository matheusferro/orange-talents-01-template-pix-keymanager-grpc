package br.com.zup.chave.consulta

import br.com.zup.chave.ChavePix
import br.com.zup.chave.ChaveRepository
import br.com.zup.chave.ChaveService
import br.com.zup.clients.bancoCentral.BancoCentralClient
import br.com.zup.exceptionsHandlers.exceptions.NotFoundKeyException
import io.micronaut.http.HttpStatus
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Singleton
import javax.validation.Valid

@Validated
@Singleton
class ConsultaChaveService(
    val repository: ChaveRepository,
    val bacenClient: BancoCentralClient
) {
    private val logger = LoggerFactory.getLogger(ChaveService::class.java)

    fun buscarChave(@Valid request: NovaConsultaChave): ChaveInfo {
        logger.info("Buscando chave pix: ${request.tipoBusca}")
        return when(request.tipoBusca){
            TipoBusca.INTERNA->{
                //busca por cliente id e id pix(interno)
                val responseInterno = repository.findByIdAndClienteId(
                    UUID.fromString(request.idPixConsulta.idPix),
                    request.idPixConsulta.clienteId
                ) ?: throw NotFoundKeyException("Não foi encontrado a chave requisitada.")

                //retorna objeto para ser transformado em ConsultaChavePixResponse
                return ChaveInfo.of(responseInterno)
            }
            TipoBusca.BACEN ->{
                //Busca internamente a chave pix
                val chavePix : ChavePix? = repository.findByChave(request.chave)
                if(chavePix != null){
                    return ChaveInfo.of(chavePix)
                }
                //Caso nao encontrado iremos buscar no banco central
                val responseChavePix = bacenClient.detalhesChavePix(request.chave).also { logger.info("Buscando no banco central.") }
                when(responseChavePix.status){
                    HttpStatus.OK -> responseChavePix.body().toModel()
                    else -> throw NotFoundKeyException("Não foi encontrado a chave requisitada.")
                }
            }
        }
    }
}