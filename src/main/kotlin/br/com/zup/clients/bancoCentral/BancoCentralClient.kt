package br.com.zup.clients.bancoCentral

import br.com.zup.clients.bancoCentral.request.CreatePixKeyRequest
import br.com.zup.clients.bancoCentral.request.DeletePixKeyRequest
import br.com.zup.clients.bancoCentral.response.CreatePixKeyResponse
import br.com.zup.clients.bancoCentral.response.DeletePixKeyResponse
import br.com.zup.clients.bancoCentral.response.PixKeyDetailsResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.client.annotation.Client

@Client("\${client.bancoCentral.pix}")
interface BancoCentralClient {

    @Post("/keys", consumes = [MediaType.APPLICATION_XML], processes = [MediaType.APPLICATION_XML])
    fun cadastrarChaveBancoCentral(@Body cadastroChaveRequest: CreatePixKeyRequest): HttpResponse<CreatePixKeyResponse>

    @Delete("/keys/{chavePix}", consumes = [MediaType.APPLICATION_XML], processes = [MediaType.APPLICATION_XML])
    fun deletarChaveBancoCentral(
        @PathVariable chavePix: String,
        @Body delecaoChaveRequest: DeletePixKeyRequest
    ): HttpResponse<DeletePixKeyResponse>

    @Get("/keys/{chavePix}", consumes = [MediaType.APPLICATION_XML], processes = [MediaType.APPLICATION_XML])
    fun detalhesChavePix(@PathVariable("chavePix") chavePix: String): HttpResponse<PixKeyDetailsResponse>
}