package br.com.zup.chave.cadastro

import br.com.zup.TipoConta
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Embeddable
class Conta(
    @Column(nullable = false)
    val nomeTitular: String,

    @Column(nullable = false)
    val cpfTitular: String,

    @Column(nullable = false)
    val instituicao: String,

    @Column(nullable = false)
    val agencia: String,

    @Column(nullable = false)
    val numero: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val tipoConta: TipoConta
){
    companion object {
        val ITAU_UNIBANCO_ISPB: String = "60701190"
    }
}