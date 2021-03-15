package br.com.zup.chave

import br.com.zup.KeyManagerRequest
import br.com.zup.KeyManagerResponse
import br.com.zup.KeyManagerServiceGrpc
import br.com.zup.chave.cadastro.CadastroNovaChaveService
import br.com.zup.chave.cadastro.toModel
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class ChaveGrpcServer(val service: CadastroNovaChaveService) : KeyManagerServiceGrpc.KeyManagerServiceImplBase() {

    private val logger = LoggerFactory.getLogger(ChaveGrpcServer::class.java)

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
}