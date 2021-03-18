package br.com.zup.exceptionsHandlers.handlers

import br.com.zup.exceptionsHandlers.ExceptionHandler
import br.com.zup.exceptionsHandlers.exceptions.ExistsKeyException
import io.grpc.Status
import javax.inject.Singleton

@Singleton
class ExistsKeyExceptionHandler : ExceptionHandler<ExistsKeyException> {

    override fun handle(e: ExistsKeyException): ExceptionHandler.StatusWithDetails {
        return  ExceptionHandler.StatusWithDetails(
            Status.ALREADY_EXISTS
                .withDescription(e.message)
                .withCause(e)
        )
    }

    override fun supports(e: Exception): Boolean {
        return e is ExistsKeyException
    }
}

