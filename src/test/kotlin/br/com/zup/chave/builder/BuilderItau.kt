package br.com.zup.chave.builder

import br.com.zup.TipoConta
import br.com.zup.chave.cadastro.Conta
import br.com.zup.clients.itau.response.DadosContaItauResponse
import br.com.zup.clients.itau.response.InstituicaoItauResponse
import br.com.zup.clients.itau.response.TitularItauResponse
import java.util.*

/**
 * Builder para comunicacao com o itau.
 *
 * @param pClienteId -> Identificador da conta itau.
 * @param pCPFOwner -> CPF do repsonsavel pela conta.
 */
class BuilderItau(val pClienteId: UUID, val pCPFOwner: String) {
    /**
     * Cria objeto com os dados de resposta do cliente itau
     */
    fun dadosContaItauResponse(): DadosContaItauResponse {
        return DadosContaItauResponse(
            tipo = TipoConta.CONTA_POUPANCA,
            InstituicaoItauResponse(nome = "", ispb = Conta.ITAU_UNIBANCO_ISPB),
            agencia = "0001",
            numero = "123123321",
            TitularItauResponse(id = pClienteId.toString(), nome = "Classe Teste", cpf = pCPFOwner)
        )
    }
}