package br.com.zup.exceptionsHandlers.handlers

import br.com.zup.exceptionsHandlers.ExceptionHandler
import br.com.zup.exceptionsHandlers.exceptions.NotFoundKeyException
import io.grpc.Status
import javax.inject.Singleton

@Singleton
class NotFoundKeyExceptionHandler : ExceptionHandler<NotFoundKeyException> {
    override fun handle(e: NotFoundKeyException): ExceptionHandler.StatusWithDetails {
        e.printStackTrace()
        return  ExceptionHandler.StatusWithDetails(
            Status.NOT_FOUND
                .withDescription(e.message)
                .withCause(e)
        )
    }

    override fun supports(e: Exception): Boolean {
        return e is NotFoundKeyException
    }
}