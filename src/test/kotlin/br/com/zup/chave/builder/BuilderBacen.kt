package br.com.zup.chave.builder

import br.com.zup.clients.bancoCentral.request.*
import br.com.zup.clients.bancoCentral.response.CreatePixKeyResponse
import java.time.LocalDateTime

/**
 * Builder referente a comunicacao com banco central.
 *
 * @param pKeyType -> Tipo chave pix.
 * @param pKey -> Chave pix.
 * @param pCPFOwner -> CPF responsavel pela conta.
 */
class BuilderBacen(val pKeyType: TipoChaveBacen, val pKey: String, val pCPFOwner: String) {

    /**
     * Request para o cadastro de chave pix no banco central.
     */
    fun dadosCriarChaveBasenRequest(): CreatePixKeyRequest {
        return CreatePixKeyRequest(
            keyType = pKeyType,
            key = pKey,
            bankAccount = dadosBankAccount(),
            owner = dadosOwner()
        )
    }

    /**
     * Cria objeto para resposta do banco central.
     *
     */
    fun dadosCriarChaveBasenResponse(): CreatePixKeyResponse {
        return CreatePixKeyResponse(
            keyType = pKeyType,
            key = pKey,
            bankAccount = dadosBankAccount(),
            owner = dadosOwner(),
            createdAt = LocalDateTime.now()
        )
    }

    /**
     * Cria objeto com dados da cont bancaria
     */
    fun dadosBankAccount(): BankAccount {
        return BankAccount(
            branch = "0001",
            accountNumber = "123123321",
            accountType = TipoContaBacen.SVGS
        )
    }

    /**
     * Cria objeto com dados do dono da conta bancaria.
     */
    fun dadosOwner(): Owner {
        return Owner(
            type = "NATURAL_PERSON",
            name = "Classe Teste",
            taxIdNumber = pCPFOwner
        )
    }
}