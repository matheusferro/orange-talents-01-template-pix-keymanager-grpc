package br.com.zup.exceptionsHandlers

import io.grpc.BindableService
import io.grpc.stub.StreamObserver
import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class ExceptionHandlerInterceptor (val resolver : ExceptionHandlerResolver) :MethodInterceptor<BindableService, Any?> {

    private val logger : Logger = LoggerFactory.getLogger(this::class.java)

    //Metodo preparado para receber execoes de outros metodos anotados com @ErrorHandler
    override fun intercept(context: MethodInvocationContext<BindableService, Any?>): Any? {
        try{
            //Procede com a execucao do metodo normalmente
            return context.proceed()
        }catch (e : Exception){

            logger.error("Ocorreu um erro na execucao do metodo '${context.executableMethod.name}' da classe  '${e.javaClass.name}'")

            //Busca o handler do erro ocorrido.
            val handler = resolver.resolve(e)

            //extrai status do handler.
            val status = handler.handle(e)
            GrpcEndpointArguments(context)
                .response()
                .onError(status.asRunTimeException())
            return null
        }
    }
    /**
     *
     */
    private class GrpcEndpointArguments(val context : MethodInvocationContext<BindableService, Any?>) {

        fun response(): StreamObserver<*> {
            return context.parameterValues[1] as StreamObserver<*>
        }

    }
}