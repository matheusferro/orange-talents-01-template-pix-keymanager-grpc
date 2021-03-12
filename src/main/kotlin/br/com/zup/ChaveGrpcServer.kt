package br.com.zup

import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import javax.inject.Singleton
import javax.persistence.EntityManager
import javax.transaction.Transactional

@Singleton
class ChaveGrpcServer(val entityManager: EntityManager) : KeyManagerServiceGrpc.KeyManagerServiceImplBase() {

    private val logger = LoggerFactory.getLogger(ChaveGrpcServer::class.java)

    @Transactional
    override fun cadastroChave(request: KeyManagerRequest?, responseObserver: StreamObserver<KeyManagerResponse>?) {
        logger.info("Iniciando criacao de chave pix: $request")

        request!!

        val chavePix = request.toModel()
        entityManager.persist(chavePix)

        logger.info("Chave pix criada: ${chavePix.id}")

        val response = KeyManagerResponse
            .newBuilder()
            .setIdPix(chavePix.id.toString())
            .build()
        responseObserver!!.onNext(response)
        responseObserver.onCompleted()

    }

    fun KeyManagerRequest.toModel(): ChavePix {
        return ChavePix(
            this.clientId,
            this.tipoChave.name, //Possivel usar somente this.tipoChave, porem sera cadastrado somente o index do paramttro.
            this.chave,
            this.tipoConta.name,
        )
    }
}