package br.com.zup.chave

import br.com.zup.DeleteKeyResponse
import br.com.zup.DetalheError
import br.com.zup.KeyManagerResponse
import br.com.zup.chave.cadastro.NovaChavePix
import br.com.zup.clients.bancoCentral.BancoCentralClient
import br.com.zup.clients.bancoCentral.request.DeletePixKeyRequest
import br.com.zup.clients.itau.ItauClient
import br.com.zup.validacoes.UUIDValido
import com.google.protobuf.Any
import com.google.rpc.Code
import com.google.rpc.Status
import io.grpc.protobuf.StatusProto
import io.grpc.stub.StreamObserver
import io.micronaut.http.HttpStatus
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@Validated
@Singleton
class ChaveService(
    val chaveRepository: ChaveRepository,
    val itauClient: ItauClient,
    val bacenClient: BancoCentralClient
) {

    private val logger = LoggerFactory.getLogger(ChaveService::class.java)

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

        //Cadastro chave no Banco central
        val requestBacen = novaChavePix.toRequestBacen(conta)
        val responseBacen =
            bacenClient.cadastrarChaveBancoCentral(requestBacen).also { logger.info("4- Cadastro no bancoCentral.") }

        if (responseBacen.status != HttpStatus.CREATED) {
            //TODO: throw ExceptionNotCreatedBacen()
            logger.warn("Chave existente !")
            val chaveExistente = Status.newBuilder()
                .setCode(Code.ALREADY_EXISTS.number)
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
        //cadastro
        val chave = novaChavePix.toModel(conta, responseBacen.body()!!.key)
        chaveRepository.save(chave)
        //retorna id interno
        return chave.id.toString()
    }

    @Transactional
    fun removerChavePix(
        @NotBlank @UUIDValido idPix: String,
        @NotBlank @UUIDValido clienteId: String,
        responseObserver: StreamObserver<DeleteKeyResponse>
    ) {
        logger.info("2- Verificando existencia de chave pix.")

        //verifica resultado com nossa base com CPF, e id pix
        val dadosChavePix: ChavePix? =
            chaveRepository.findByIdAndClienteId(UUID.fromString(idPix), clienteId)

        if (dadosChavePix == null) {
            logger.warn("Chave pix nao encontrada.")
            //TODO: throw ExceptionNotFoundPixKey
            val notExistsErro = Status.newBuilder()
                .setCode(Code.NOT_FOUND_VALUE)
                .setMessage("Não foi possível excluir chave pix.")
                .addDetails(
                    com.google.protobuf.Any.pack(
                        DetalheError.newBuilder()
                            .setCodigo(404)
                            .setMensagem("Não foi encontrado chave pix para esse cliente.")
                            .build()
                    )
                ).build()
            responseObserver.onError(StatusProto.toStatusRuntimeException(notExistsErro))
            return
        }
        //Deletar do banco central
        val deleteResponse =
            bacenClient.deletarChaveBancoCentral(dadosChavePix.chave, DeletePixKeyRequest(dadosChavePix.chave))
                .also { logger.info("4- Deletando chave do banco central.") }

        if (deleteResponse.status != HttpStatus.OK) {
            //TODO Exception throw :Não foi possivel deletar
            throw IllegalStateException("Não foi possivel deletar")
        }

        chaveRepository.delete(dadosChavePix)
        logger.info("Chave deletada.")
    }
}