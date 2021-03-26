package br.com.zup.chave.builder

import br.com.zup.TipoChave
import br.com.zup.TipoConta
import br.com.zup.chave.ChavePix
import br.com.zup.chave.cadastro.Conta

class ChavePixEntity() {

    companion object {
        fun chavePixEntity(
            clienteCPF: String,
            clienteId: String,
            tipoChave: TipoChave,
            chave: String
        ): ChavePix {
            return ChavePix(
                clienteId,
                tipoChave,
                chave,
                conta = Conta(
                    "Classe Teste",
                    clienteCPF,
                    "TESTE",
                    "0001",
                    "000000000",
                    TipoConta.CONTA_POUPANCA
                )
            )
        }
    }
}