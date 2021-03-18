package br.com.zup.chave.consulta

import br.com.zup.ConsultaChavePixRequest
import br.com.zup.ConsultaChavePixResponse
import br.com.zup.KeyManagerConsultaServiceGrpc
import br.com.zup.chave.toModel
import br.com.zup.exceptionsHandlers.ErrorHandler
import io.grpc.stub.StreamObserver
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@ErrorHandler
@Singleton
class ConsultaChaveGrpcServer(val service: ConsultaChaveService) :
    KeyManagerConsultaServiceGrpc.KeyManagerConsultaServiceImplBase() {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun consultaChavePix(
        request: ConsultaChavePixRequest?,
        responseObserver: StreamObserver<ConsultaChavePixResponse>?
    ) {
        logger.info("1- Iniciando busca de chave pix.")
        //Verificando se request e response esta null
        request ?: throw  IllegalArgumentException()
        responseObserver ?: throw IllegalArgumentException()

        var response: ChaveInfo = service.buscarChave(request.toModel())

        responseObserver.onNext(ConsultaChavePixResponseConverter().toResponse(response))
        responseObserver.onCompleted()
    }
}