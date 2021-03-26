package br.com.zup.chave.consulta

import br.com.zup.ConsultaChavePixRequest
import br.com.zup.IdPixConsulta
import br.com.zup.KeyManagerConsultaServiceGrpc
import br.com.zup.TipoChave
import br.com.zup.chave.ChavePix
import br.com.zup.chave.ChaveRepository
import br.com.zup.chave.builder.ChavePixEntity
import br.com.zup.clients.bancoCentral.BancoCentralClient
import io.grpc.ManagedChannel
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.http.HttpResponse
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.*
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject

@MicronautTest(transactional = false)
class ConsultaChaveTeste(
    val grpcClientConsulta: KeyManagerConsultaServiceGrpc.KeyManagerConsultaServiceBlockingStub,
    val chaveRepository: ChaveRepository
) {

    @Inject
    lateinit var bacenClient: BancoCentralClient

    lateinit var CHAVE_EXISTENTE: ChavePix

    private val CLIENTE_ID = UUID.randomUUID().toString()
    private val CLIENTE_CPF = "31069461059"
    private val ID_PIX = UUID.randomUUID()
    private var CHAVE_PIX: ChavePix? = null

    @BeforeEach
    fun setup() {
        CHAVE_EXISTENTE = chaveRepository.save(
            ChavePixEntity.chavePixEntity(
                clienteCPF = CLIENTE_CPF,
                clienteId = CLIENTE_ID,
                tipoChave = TipoChave.CPF,
                chave = CLIENTE_CPF
            )
        )

        chaveRepository.save(
            ChavePixEntity.chavePixEntity(
                clienteCPF = CLIENTE_CPF,
                clienteId = CLIENTE_ID,
                tipoChave = TipoChave.ALEATORIA,
                chave = "random1"
            )
        )

        chaveRepository.save(
            ChavePixEntity.chavePixEntity(
                clienteCPF = CLIENTE_CPF,
                clienteId = CLIENTE_ID,
                tipoChave = TipoChave.ALEATORIA,
                chave = "random2"
            )
        )

        chaveRepository.save(
            ChavePixEntity.chavePixEntity(
                clienteCPF = "78461887042",
                clienteId = UUID.randomUUID().toString(),
                tipoChave = TipoChave.ALEATORIA,
                chave = "random3"
            )
        )
    }

    @AfterEach
    fun deleteAll() {
        chaveRepository.deleteAll()
    }

    @Test
    fun `deve retornar dados da chave retornada pelo id e cliente`() {
        val requestGrpc = ConsultaChavePixRequest.newBuilder()
            .setChave("")
            .setIdPix(
                IdPixConsulta.newBuilder()
                    .setClienteId(CLIENTE_ID)
                    .setIdPix(CHAVE_EXISTENTE.id.toString())
                    .build()
            )
            .build()
        val repsponseGrpc = grpcClientConsulta.consultaChavePix(requestGrpc)
        with(repsponseGrpc) {
            //Chave encontrada deve ser o cpf, de acordo com o que cadastramos no inicio.
            Assertions.assertEquals(CLIENTE_CPF, this.chave)
            Assertions.assertEquals(CHAVE_EXISTENTE.id.toString(), this.idPix)
            Assertions.assertEquals(CHAVE_EXISTENTE.clienteId, this.clienteId)
        }
    }

    @Test
    fun `deve retornar dados da chave pesquisada`() {
        val requestGrpc = ConsultaChavePixRequest.newBuilder()
            .setChave("random1")
            .build()

        val repsponseGrpc = grpcClientConsulta.consultaChavePix(requestGrpc)
        with(repsponseGrpc) {
            Assertions.assertEquals("random1", this.chave)
            Assertions.assertEquals(TipoChave.ALEATORIA, this.tipoChave)
        }
    }

    @Test
    fun `nao deve encontrar chave pix`() {
        val requestGrpc = ConsultaChavePixRequest.newBuilder()
            .setChave("inexistente")
            .build()

        //tentativa de consulta no banco central
        Mockito.`when`(bacenClient.detalhesChavePix("inexistente"))
            .thenReturn(HttpResponse.notFound())

        //Verificando se realmente foi lancado excecao.
        val exception = assertThrows<StatusRuntimeException> { grpcClientConsulta.consultaChavePix(requestGrpc) }
        Assertions.assertEquals("NOT_FOUND: Não foi encontrado a chave requisitada.", exception.message)
    }

    @MockBean(BancoCentralClient::class)
    fun mockBacen(): BancoCentralClient? {
        return Mockito.mock(BancoCentralClient::class.java)
    }

    @Factory
    class ClientConsulta {
        @Bean
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): KeyManagerConsultaServiceGrpc.KeyManagerConsultaServiceBlockingStub? {
            return KeyManagerConsultaServiceGrpc.newBlockingStub(channel)
        }
    }
}