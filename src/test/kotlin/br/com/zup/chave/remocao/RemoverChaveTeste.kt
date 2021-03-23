package br.com.zup.chave.remocao

import br.com.zup.DeleteKeyRequest
import br.com.zup.KeyManagerServiceGrpc
import br.com.zup.TipoChave
import br.com.zup.TipoConta
import br.com.zup.chave.ChavePix
import br.com.zup.chave.ChaveRepository
import br.com.zup.chave.cadastro.Conta
import br.com.zup.clients.bancoCentral.BancoCentralClient
import br.com.zup.clients.bancoCentral.request.DeletePixKeyRequest
import io.micronaut.http.HttpResponse
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject

@MicronautTest(transactional = false)
internal class RemoverChaveTeste(
    val grpcClientRemover: KeyManagerServiceGrpc.KeyManagerServiceBlockingStub,
    val chaveRepository: ChaveRepository
) {

    @Inject
    lateinit var bacenClient: BancoCentralClient

    private val CLIENTE_ID = UUID.randomUUID().toString()
    private val CLIENTE_CPF = "31069461059"
    private val ID_PIX = UUID.randomUUID()
    private var CHAVE_PIX: ChavePix? = null

    @BeforeEach
    fun setup() {
        CHAVE_PIX = chaveRepository.save(
            chavePixEntity(clienteId = CLIENTE_ID, tipoChave = TipoChave.CPF, chave = CLIENTE_CPF)
        )
    }

    @Test
    fun `deve excluir uma chave`() {
        //mock deletarChaveBancoCentral
        Mockito.`when`(bacenClient.deletarChaveBancoCentral(CLIENTE_CPF, DeletePixKeyRequest(CLIENTE_CPF)))
            .thenReturn(HttpResponse.ok())

        val requestGrpc = DeleteKeyRequest.newBuilder()
            .setClienteId(CLIENTE_ID)
            .setIdPix(CHAVE_PIX!!.id.toString())
            .build()
        val responseGrpc = grpcClientRemover.deletarChave(requestGrpc)
        with(responseGrpc) {
            Assertions.assertNotNull(idPix)
            Assertions.assertNotNull(clienteId)
        }
    }

    private fun chavePixEntity(
        clienteId: String,
        tipoChave: TipoChave,
        chave: String
    ): ChavePix {
        return ChavePix(
            clienteId,
            tipoChave,
            chave,
            conta = Conta(
                "Classe Teste",
                CLIENTE_CPF,
                "TESTE",
                "0001",
                "000000000",
                TipoConta.CONTA_POUPANCA
            )
        )
    }

    @MockBean(BancoCentralClient::class)
    fun bacenClient(): BancoCentralClient? {
        return Mockito.mock(BancoCentralClient::class.java)
    }
}