package br.com.zup

import java.time.LocalDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class ChavePix(
    @Column(nullable = false)
    val clienteId: String,

    @Column(nullable = false)
    val tipoChave: String,

    @Column(nullable = false, unique = true)
    val chave: String,

    @Column(nullable = false)
    val tipoConta: String
) {
    @Id
    var id: UUID = UUID.randomUUID()

    val criadoEm: LocalDateTime = LocalDateTime.now()
}