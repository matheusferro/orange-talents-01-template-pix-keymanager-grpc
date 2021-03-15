package br.com.zup.chave.cadastro

import br.com.zup.DetalheError
import br.com.zup.KeyManagerResponse
import br.com.zup.chave.ChaveRepository
import br.com.zup.clients.ItauClient
import com.google.protobuf.Any
import com.google.rpc.Status
import io.grpc.protobuf.StatusProto
import io.grpc.stub.StreamObserver
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Singleton
class CadastroNovaChaveService(
    val chaveRepository: ChaveRepository,
    val itauClient: ItauClient
) {

    private val logger = LoggerFactory.getLogger(CadastroNovaChaveService::class.java)

    @Transactional
    fun cadastro(@Valid novaChavePix: NovaChavePix, responseObserver: StreamObserver<KeyManagerResponse>): String {

        if (chaveRepository.existsByChave(novaChavePix.chave)) {
            logger.warn("Chave existente !")
            val chaveExistente = Status.newBuilder()
                .setCode(com.google.rpc.Code.ALREADY_EXISTS.number)
                .setMessage("Não foi possível realizar o cadastro")
                .addDetails(
                    Any.pack(
                        DetalheError.newBuilder()
                            .setCodigo(422)
                            .setMensagem("Chave já cadastrada.")
                            .build()
                    )
                ).build()

            responseObserver.onError(StatusProto.toStatusRuntimeException(chaveExistente))
            return "chave existente."
        }

        //Busca dados cliente - itau
        logger.info("2- Busca dados conta cliente. ")
        val dadosContaItau = itauClient.buscarDadosContaCliente(novaChavePix.clientId, novaChavePix.tipoConta.name)
        logger.info("3- Busca completa. ")

        //toModel da resposta do itau e verificacao
        val conta = dadosContaItau?.toModel() ?: throw IllegalStateException("Cliente não encontrado")

        //cadastro
        val chave = novaChavePix.toModel(conta)
        chaveRepository.save(chave)

        //retorna id interno
        return chave.id.toString()
    }
}