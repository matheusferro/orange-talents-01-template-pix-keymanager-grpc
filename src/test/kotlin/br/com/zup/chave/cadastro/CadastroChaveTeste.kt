package br.com.zup.chave.cadastro

import br.com.zup.KeyManagerRequest
import br.com.zup.KeyManagerServiceGrpc
import br.com.zup.TipoChave
import br.com.zup.TipoConta
import br.com.zup.chave.ChaveRepository
import br.com.zup.chave.builder.BuilderBacen
import br.com.zup.chave.builder.BuilderItau
import br.com.zup.clients.bancoCentral.BancoCentralClient
import br.com.zup.clients.bancoCentral.request.TipoChaveBacen
import br.com.zup.clients.itau.ItauClient
import io.grpc.ManagedChannel
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.http.HttpResponse
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject


@MicronautTest(transactional = false)
internal class CadastroChaveTeste(
    val grpcClient: KeyManagerServiceGrpc.KeyManagerServiceBlockingStub,
    val chaveRepository: ChaveRepository
) {

    @Inject
    lateinit var itauClient: ItauClient

    @Inject
    lateinit var bacenClient: BancoCentralClient

    private val CLIENTE_ID = UUID.randomUUID()
    private val CLIENTE_CPF = "31069461059"

    @BeforeEach
    fun before() {
        chaveRepository.deleteAll()
    }

    @Test
    fun `cadastro chave pix - email`() {

        Mockito.`when`(itauClient.buscarDadosContaCliente(CLIENTE_ID.toString(), TipoConta.CONTA_POUPANCA.toString()))
            .thenReturn(HttpResponse.ok(BuilderItau(CLIENTE_ID, CLIENTE_CPF).dadosContaItauResponse()))

        val mockBacen = BuilderBacen(
            TipoChaveBacen.EMAIL,
            "email@teste.com.br",
            CLIENTE_CPF
        )
        Mockito.`when`(bacenClient.cadastrarChaveBancoCentral(mockBacen.dadosCriarChaveBasenRequest()))
            .thenReturn(HttpResponse.created(mockBacen.dadosCriarChaveBasenResponse()))

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

    @Test
    fun `cadastro chave pix - CPF`() {

        Mockito.`when`(itauClient.buscarDadosContaCliente(CLIENTE_ID.toString(), TipoConta.CONTA_POUPANCA.toString()))
            .thenReturn(HttpResponse.ok(BuilderItau(CLIENTE_ID, CLIENTE_CPF).dadosContaItauResponse()))

        val mockBacen = BuilderBacen(
            TipoChaveBacen.CPF,
            CLIENTE_CPF,
            CLIENTE_CPF
        )
        Mockito.`when`(bacenClient.cadastrarChaveBancoCentral(mockBacen.dadosCriarChaveBasenRequest()))
            .thenReturn(HttpResponse.created(mockBacen.dadosCriarChaveBasenResponse()))

        val requestGrpc = KeyManagerRequest.newBuilder()
            .setClienteId(CLIENTE_ID.toString())
            .setChave(CLIENTE_CPF)
            .setTipoChave(TipoChave.CPF)
            .setTipoConta(TipoConta.CONTA_POUPANCA)
            .build()

        val responseGrpc = grpcClient.cadastroChave(requestGrpc)
        with(responseGrpc) {
            assertNotNull(idPix)
        }
    }

    @Test
    fun `cadastro chave pix - telefone celular`() {

        Mockito.`when`(itauClient.buscarDadosContaCliente(CLIENTE_ID.toString(), TipoConta.CONTA_POUPANCA.toString()))
            .thenReturn(HttpResponse.ok(BuilderItau(CLIENTE_ID, CLIENTE_CPF).dadosContaItauResponse()))

        val mockBacen = BuilderBacen(
            TipoChaveBacen.PHONE,
            "+5511999999999",
            CLIENTE_CPF
        )
        Mockito.`when`(bacenClient.cadastrarChaveBancoCentral(mockBacen.dadosCriarChaveBasenRequest()))
            .thenReturn(HttpResponse.created(mockBacen.dadosCriarChaveBasenResponse()))

        val requestGrpc = KeyManagerRequest.newBuilder()
            .setClienteId(CLIENTE_ID.toString())
            .setChave("+5511999999999")
            .setTipoChave(TipoChave.TELEFONE_CELULAR)
            .setTipoConta(TipoConta.CONTA_POUPANCA)
            .build()

        val responseGrpc = grpcClient.cadastroChave(requestGrpc)
        with(responseGrpc) {
            assertNotNull(idPix)
        }
    }

    @Test
    fun `cadastro chave pix - aleatoria`() {

        Mockito.`when`(itauClient.buscarDadosContaCliente(CLIENTE_ID.toString(), TipoConta.CONTA_POUPANCA.toString()))
            .thenReturn(HttpResponse.ok(BuilderItau(CLIENTE_ID, CLIENTE_CPF).dadosContaItauResponse()))

        val mockBacen = BuilderBacen(
            TipoChaveBacen.RANDOM,
            "",
            CLIENTE_CPF
        )
        Mockito.`when`(bacenClient.cadastrarChaveBancoCentral(mockBacen.dadosCriarChaveBasenRequest()))
            .thenReturn(HttpResponse.created(mockBacen.dadosCriarChaveBasenResponse()))

        val requestGrpc = KeyManagerRequest.newBuilder()
            .setClienteId(CLIENTE_ID.toString())
            .setChave("")
            .setTipoChave(TipoChave.ALEATORIA)
            .setTipoConta(TipoConta.CONTA_POUPANCA)
            .build()

        val responseGrpc = grpcClient.cadastroChave(requestGrpc)
        with(responseGrpc) {
            assertNotNull(idPix)
        }
    }

    @Test
    fun `cadastro chave pix - email existente`() {

        Mockito.`when`(itauClient.buscarDadosContaCliente(CLIENTE_ID.toString(), TipoConta.CONTA_POUPANCA.toString()))
            .thenReturn(HttpResponse.ok(BuilderItau(CLIENTE_ID, CLIENTE_CPF).dadosContaItauResponse()))

        val mockBacen = BuilderBacen(
            TipoChaveBacen.EMAIL,
            "email@teste.com.br",
            CLIENTE_CPF
        )
        Mockito.`when`(bacenClient.cadastrarChaveBancoCentral(mockBacen.dadosCriarChaveBasenRequest()))
            .thenReturn(HttpResponse.created(mockBacen.dadosCriarChaveBasenResponse()))

        val requestGrpc = KeyManagerRequest.newBuilder()
            .setClienteId(CLIENTE_ID.toString())
            .setChave("email@teste.com.br")
            .setTipoChave(TipoChave.EMAIL)
            .setTipoConta(TipoConta.CONTA_POUPANCA)
            .build()
        //Cadastro da primeira chave sem erro.
        grpcClient.cadastroChave(requestGrpc)

        //Cadastro da chave repetida.
        val exception = assertThrows<StatusRuntimeException> { grpcClient.cadastroChave(requestGrpc) }
        assertEquals("ALREADY_EXISTS: Chave já existente.", exception.message)
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