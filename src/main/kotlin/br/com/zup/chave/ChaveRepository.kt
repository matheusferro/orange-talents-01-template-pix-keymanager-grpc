package br.com.zup.chave

import br.com.zup.chave.consulta.ChavePixCliente
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface ChaveRepository : JpaRepository<ChavePix, UUID> {

    fun existsByChave(chave: String): Boolean

    fun findByIdAndClienteId(id: UUID, clienteId: String): ChavePix?

    fun findByChave(chave: String): ChavePix?

    @Query("SELECT new br.com.zup.chave.consulta.ChavePixCliente(pix.id, pix.clienteId, pix.tipoChave, pix.chave, pix.conta.tipoConta, pix.criadoEm) FROM ChavePix pix WHERE pix.clienteId = :pClienteId")
    fun findByClienteId(pClienteId: String): List<ChavePixCliente>?
}