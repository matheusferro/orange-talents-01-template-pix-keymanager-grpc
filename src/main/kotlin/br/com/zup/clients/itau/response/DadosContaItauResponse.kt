package br.com.zup.clients.itau.response

import br.com.zup.TipoConta
import br.com.zup.chave.cadastro.Conta

data class DadosContaItauResponse(
    val tipo: TipoConta,
    val instituicao: InstituicaoItauResponse,
    val agencia: String,
    val numero: String,
    val titular: TitularItauResponse
) {
    fun toModel(): Conta {
        return Conta(titular.nome, titular.cpf, instituicao.nome, agencia, numero, tipo)
    }
}

data class InstituicaoItauResponse(
    val nome: String,
    val ispb: String
)

data class TitularItauResponse(
    val id: String,
    val nome: String,
    val cpf: String
)

