package br.com.zup.clients.bancoCentral.request

import br.com.zup.chave.cadastro.Conta

/**
 * Body requisicao para deletar chave pix no banco central.
 *
 * @param key   -> chave pix.
 */
data class DeletePixKeyRequest(
    val key: String
){
    val participant: String = Conta.ITAU_UNIBANCO_ISPB //ISPB (Identificador de Sistema de Pagamento Brasileiro)
}
