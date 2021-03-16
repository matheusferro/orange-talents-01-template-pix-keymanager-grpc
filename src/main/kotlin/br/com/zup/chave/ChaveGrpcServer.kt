package br.com.zup.chave

import br.com.zup.*
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class ChaveGrpcServer(
    val service: ChaveService
) : KeyManagerServiceGrpc.KeyManagerServiceImplBase() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun cadastroChave(request: KeyManagerRequest?, responseObserver: StreamObserver<KeyManagerResponse>?) {

        logger.info("1- Iniciando criacao de chave pix.")

        //Verificando se request e response esta null
        request ?: throw  IllegalArgumentException()
        responseObserver ?: throw IllegalArgumentException()

        //processando
        val chavePix = request.toModel()
        val chaveCriada = service.cadastro(chavePix, responseObserver)

        val response = KeyManagerResponse
            .newBuilder()
            .setIdPix(chaveCriada)
            .build()

        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }

    override fun deletarChave(request: DeleteKeyRequest?, responseObserver: StreamObserver<DeleteKeyResponse>?) {

        logger.info("1- Iniciando delecao de chave pix.")

        //Verificando se request e response esta null
        request ?: throw  IllegalArgumentException()
        responseObserver ?: throw IllegalArgumentException()

        service.removerChavePix(request.idPix, request.clienteId, responseObserver)

        val response = DeleteKeyResponse
            .newBuilder()
            .setClienteId(request.clienteId)
            .setIdPix(request.idPix)
            .build()

        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}