package br.com.zup.exceptionsHandlers

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.Metadata
import io.grpc.protobuf.StatusProto

interface ExceptionHandler<T : Exception> {

    fun handle(e: T): StatusWithDetails

    fun supports(e: Exception): Boolean
    
    data class StatusWithDetails(val status: Status, val metadata: Metadata = Metadata()){
        constructor(statusExp : StatusRuntimeException): this(statusExp.status, statusExp.trailers ?: Metadata())
        constructor(statusProto : com.google.rpc.Status): this(StatusProto.toStatusRuntimeException(statusProto))

        fun asRunTimeException(): StatusRuntimeException{
            return status.asRuntimeException(metadata)
        }
    }
}
