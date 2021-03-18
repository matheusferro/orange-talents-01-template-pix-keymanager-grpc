package br.com.zup.chave.consulta

import br.com.zup.ConsultaChavePixResponse
import br.com.zup.DadosConta
import br.com.zup.TipoChave
import com.google.protobuf.Timestamp
import java.time.temporal.ChronoField

class ConsultaChavePixResponseConverter {
    fun toResponse(chaveInfo : ChaveInfo): ConsultaChavePixResponse {
        return ConsultaChavePixResponse.newBuilder()
            .setIdPix(chaveInfo.idPix?.toString() ?: "")
            .setClienteId(chaveInfo.clienteId?.toString() ?: "")
            .setTipoChave(TipoChave.valueOf(chaveInfo.tipoChave.name))
            .setChave(chaveInfo.chave)
            .setNomeTitular(chaveInfo.conta.nomeTitular)
            .setCpfTitular(chaveInfo.conta.cpfTitular)
            .setCriadaEm(
                Timestamp.newBuilder()
                    .setSeconds(chaveInfo.criadoEm.getLong(ChronoField.SECOND_OF_DAY))
                    .setNanos(chaveInfo.criadoEm.nano)
                    .build()
            )
            .setDadosConta(
                DadosConta.newBuilder()
                    .setInstituicao(chaveInfo.conta.instituicao)
                    .setAgencia(chaveInfo.conta.agencia)
                    .setNumero(chaveInfo.conta.numero)
                    .setTipoConta(chaveInfo.conta.tipoConta)
                    .build()
            )
            .build()
    }
}