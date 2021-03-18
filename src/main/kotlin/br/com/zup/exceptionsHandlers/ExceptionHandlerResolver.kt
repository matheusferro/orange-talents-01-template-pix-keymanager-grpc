package br.com.zup.exceptionsHandlers

import br.com.zup.exceptionsHandlers.handlers.DefaultExceptionHandler
import javax.inject.Singleton

@Singleton
class ExceptionHandlerResolver(val handlers : List<ExceptionHandler<Exception>>) {

    private var defaultHandler : ExceptionHandler<Exception> = DefaultExceptionHandler()

    constructor(handlers : List<ExceptionHandler<Exception>>, defaultHandler : ExceptionHandler<Exception>) : this(handlers){
        this.defaultHandler = defaultHandler
    }

    fun resolve(exp : Exception) : ExceptionHandler<Exception>{
        //handlers injetado. supports retorna se o exception recebido pertence ao handler.
        val foundHandlers = handlers.filter { h -> h.supports(exp) }
        if (foundHandlers.size > 1) {
            throw IllegalStateException("Erro inesperado. Mais de um handler para o erro: '${exp.javaClass.name}': $foundHandlers")
        }

        //Caso nao foi criado handler para o erro, e retornado um handler padrao
        return foundHandlers.firstOrNull() ?: defaultHandler
    }

}
