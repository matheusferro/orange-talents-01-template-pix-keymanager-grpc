package br.com.zup.exceptionsHandlers

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.Metadata
import io.grpc.protobuf.StatusProto

interface ExceptionHandler<T : Exception> {

    //metodo para construir corpo da resposta
    fun handle(e: T): StatusWithDetails

    //para verificacao entre exception e handler
    fun supports(e: Exception): Boolean

    //Wrapper de Status e Metadata
    data class StatusWithDetails(val status: Status, val metadata: Metadata = Metadata()){
        constructor(statusExp : StatusRuntimeException): this(statusExp.status, statusExp.trailers ?: Metadata())
        constructor(statusProto : com.google.rpc.Status): this(StatusProto.toStatusRuntimeException(statusProto))

        fun asRunTimeException(): StatusRuntimeException{
            return status.asRuntimeException(metadata)
        }
    }
}
