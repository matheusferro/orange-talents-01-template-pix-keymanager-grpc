package br.com.zup.exceptionsHandlers.handlers

import br.com.zup.exceptionsHandlers.ExceptionHandler
import com.google.rpc.BadRequest
import com.google.rpc.Code
import javax.inject.Singleton
import javax.validation.ConstraintViolationException

@Singleton
class ContraintViolationHandler: ExceptionHandler<ConstraintViolationException> {
    override fun handle(e: ConstraintViolationException): ExceptionHandler.StatusWithDetails {
        val badRequest = BadRequest.newBuilder() // com.google.rpc.BadRequest
            .addAllFieldViolations(e.constraintViolations.map {
                BadRequest.FieldViolation.newBuilder()
                    .setField(it.propertyPath.last().name) // propertyPath=save.entity.email
                    .setDescription(it.message)
                    .build()
            }
            ).build()

        val statusProto = com.google.rpc.Status.newBuilder()
            .setCode(Code.INVALID_ARGUMENT_VALUE)
            .setMessage("Valores invalidos.")
            .addDetails(com.google.protobuf.Any.pack(badRequest)) // com.google.protobuf.Any
            .build()


        return ExceptionHandler.StatusWithDetails(statusProto)
    }

    override fun supports(e: Exception): Boolean {
        return e is ConstraintViolationException
    }

}