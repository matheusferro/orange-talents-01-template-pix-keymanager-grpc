package br.com.zup.exceptionsHandlers.handlers

import br.com.zup.exceptionsHandlers.ExceptionHandler
import br.com.zup.exceptionsHandlers.exceptions.CreateKeyPixBacenFailed
import io.grpc.Status
import javax.inject.Singleton

@Singleton
class CreateKeyPixBacenFailedHandler : ExceptionHandler<CreateKeyPixBacenFailed> {

    override fun handle(e: CreateKeyPixBacenFailed): ExceptionHandler.StatusWithDetails {
        return  ExceptionHandler.StatusWithDetails(
            Status.ABORTED
                .withDescription(e.message)
                .withCause(e)
        )
    }

    override fun supports(e: Exception): Boolean {
        return e is CreateKeyPixBacenFailed
    }
}