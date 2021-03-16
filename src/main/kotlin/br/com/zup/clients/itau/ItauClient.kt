package br.com.zup.clients.itau

import br.com.zup.clients.itau.response.DadosContaItauResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client

@Client("\${client.itau.contas}")
interface ItauClient {

    @Get("/clientes/{clienteId}/contas{?tipo}")
    fun buscarDadosContaCliente(@PathVariable clienteId: String, @QueryValue tipo: String): DadosContaItauResponse?
}