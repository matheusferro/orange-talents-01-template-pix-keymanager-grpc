package br.com.zup.chave

import br.com.zup.KeyManagerRequest
import br.com.zup.KeyManagerServiceGrpc
import br.com.zup.TipoChave
import br.com.zup.TipoConta
import br.com.zup.chave.cadastro.Conta
import br.com.zup.clients.bancoCentral.BancoCentralClient
import br.com.zup.clients.bancoCentral.request.*
import br.com.zup.clients.bancoCentral.response.CreatePixKeyResponse
import br.com.zup.clients.itau.ItauClient
import br.com.zup.clients.itau.response.DadosContaItauResponse
import br.com.zup.clients.itau.response.InstituicaoItauResponse
import br.com.zup.clients.itau.response.TitularItauResponse
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.http.HttpResponse
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject


@MicronautTest(transactional = false)
internal class CadastroChaveTeste(
    var grpcClient: KeyManagerServiceGrpc.KeyManagerServiceBlockingStub
) {

    @Inject
    lateinit var itauClient: ItauClient

    @Inject
    lateinit var bacenClient: BancoCentralClient

    val CLIENTE_ID = UUID.randomUUID()

    @Test
    fun `cadastro chave pix - email`() {

        Mockito.`when`(itauClient.buscarDadosContaCliente(CLIENTE_ID.toString(), TipoConta.CONTA_POUPANCA.toString()))
            .thenReturn(HttpResponse.ok(dadosContaItauResponse()))

        Mockito.`when`(bacenClient.cadastrarChaveBancoCentral(dadosCriarChaveBasenRequest()))
            .thenReturn(HttpResponse.created(dadosCriarChaveBasenResponse()))

        val requestGrpc = KeyManagerRequest.newBuilder()
            .setClienteId(CLIENTE_ID.toString())
            .setChave("email@teste.com.br")
            .setTipoChave(TipoChave.EMAIL)
            .setTipoConta(TipoConta.CONTA_POUPANCA)
            .build()

        val responseGrpc = grpcClient.cadastroChave(requestGrpc)
        with(responseGrpc) {
            assertNotNull(idPix)
        }
    }

    //Cria objeto com os dados de resposta do cliente itau
    fun dadosContaItauResponse(): DadosContaItauResponse {
        return DadosContaItauResponse(
            tipo = TipoConta.CONTA_POUPANCA,
            InstituicaoItauResponse(nome = "", ispb = Conta.ITAU_UNIBANCO_ISPB),
            agencia = "0001",
            numero = "123123321",
            TitularItauResponse(id = CLIENTE_ID.toString(), nome = "Classe Teste", cpf = "31069461059")
        )
    }

    //Montar dados da request cadastro chave banco central
    fun dadosCriarChaveBasenRequest(): CreatePixKeyRequest {
        return CreatePixKeyRequest(
            keyType = TipoChaveBacen.EMAIL,
            key = "email@teste.com.br",
            bankAccount = dadosBankAccount(),
            owner = dadosOwner()
        )
    }

    //Cria objeto para resposta do banco central
    fun dadosCriarChaveBasenResponse(): CreatePixKeyResponse {
        return CreatePixKeyResponse(
            keyType = TipoChaveBacen.EMAIL,
            key = "email@teste.com.br",
            bankAccount = dadosBankAccount(),
            owner = dadosOwner(),
            createdAt = LocalDateTime.now()
        )
    }

    //Cria objeto com dados da cont bancaria
    fun dadosBankAccount(): BankAccount {
        return BankAccount(
            branch = "0001",
            accountNumber = "123123321",
            accountType = TipoContaBacen.SVGS
        )
    }

    //Cria objeto com dados do dono da conta bancaria
    fun dadosOwner(): Owner {
        return Owner(
            type = "NATURAL_PERSON",
            name = "Classe Teste",
            taxIdNumber = "31069461059"
        )
    }

    //Definindo mock do client do banco central
    @MockBean(BancoCentralClient::class)
    fun bacenClient(): BancoCentralClient? {
        return Mockito.mock(BancoCentralClient::class.java)
    }

    //Definindo mock do client itau
    @MockBean(ItauClient::class)
    fun itauClient(): ItauClient? {
        return Mockito.mock(ItauClient::class.java)
    }

    //Definindo client de teste
    @Factory
    class Clients {
        /**
         *
         * @param channel : ManagedChannel          -> Destinado para comunicacao com o servidor.
         * @return KeyManagerServiceBlockingStub    -> Blocking stub é criado.
         */
        @Bean
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): KeyManagerServiceGrpc.KeyManagerServiceBlockingStub? {
            return KeyManagerServiceGrpc.newBlockingStub(channel)
        }
    }
}