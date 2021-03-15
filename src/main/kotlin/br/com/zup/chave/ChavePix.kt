package br.com.zup.chave

import br.com.zup.TipoChave
import br.com.zup.chave.cadastro.Conta
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
class ChavePix(
    @Column(nullable = false)
    val clienteId: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val tipoChave: TipoChave,

    @Column(nullable = false, unique = true, length = 77)
    val chave: String,

    @Column(nullable = false)
    @Embedded
    val conta: Conta
) {
    @Id
    var id: UUID = UUID.randomUUID()

    val criadoEm: LocalDateTime = LocalDateTime.now()

    override fun toString(): String {
        return "ChavePix(clienteId='$clienteId', tipoChave=$tipoChave, chave='$chave', conta=$conta, id=$id, criadoEm=$criadoEm)"
    }
}