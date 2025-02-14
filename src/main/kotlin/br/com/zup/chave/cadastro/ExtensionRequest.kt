package br.com.zup.chave.cadastro

import br.com.zup.KeyManagerRequest

fun KeyManagerRequest.toModel(): NovaChavePix {
    return NovaChavePix(
        this.clienteId,
        (br.com.zup.chave.TipoChaveComValida.valueOf(tipoChave.name)),
        this.chave,
        this.tipoConta,
    )
}