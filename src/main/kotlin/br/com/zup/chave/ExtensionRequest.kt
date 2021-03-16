package br.com.zup.chave

import br.com.zup.DeleteKeyRequest
import br.com.zup.KeyManagerRequest
import br.com.zup.chave.cadastro.NovaChavePix
import br.com.zup.chave.remocao.RemoverChaveRequest

fun KeyManagerRequest.toModel(): NovaChavePix {
    return NovaChavePix(
        this.clienteId,
        (TipoChaveComValida.valueOf(tipoChave.name)),
        this.chave,
        this.tipoConta,
    )
}

fun DeleteKeyRequest.toModel(): RemoverChaveRequest{
    return RemoverChaveRequest(
        this.idPix,
        this.clienteId
    )
}