package br.com.zup.clients.bancoCentral.request

import br.com.zup.TipoConta
import br.com.zup.chave.TipoChaveComValida
import br.com.zup.chave.cadastro.Conta

/**
 * Request para criar chave pix no banco central.
 *
 * @param keyType       -> Tipo da chave: [CPF, RANDOM, EMAIL, CNPJ, PHONE]
 * @param key           -> Chave pix.
 * @param bankAccount   -> Informacoes sobre a conta.       Tipo BankAccount.
 * @param owner         -> Informacoes sobre o responsavel. Tipo Owner.
 */
data class CreatePixKeyRequest(
    val keyType: TipoChaveBacen,
    val key: String,
    val bankAccount: BankAccount,
    val owner: Owner
)

/**
 * Classe que compoe o request para criacao de chave pix
 * no banco central. Contem informacoes da conta.
 *
 * @param branch            -> Agencia da conta.
 * @param accountNumber     -> Numero da conta.
 * @param accountType       -> Tipo de conta. [CACC (Current), SVGS (Savings)]
 */
data class BankAccount(
    val branch: String,
    val accountNumber: String,
    val accountType: TipoContaBacen,
    val participant: String = Conta.ITAU_UNIBANCO_ISPB
)

/**
 * Classe com informacoes referente ao responsavel da conta,
 * para efetuar cadastro de chave pix no banco central.
 *
 * @param type              -> Pessoa: [LEGAL_PERSON, NATURAL_PERSON]
 * @param name              -> Nome
 * @param taxIdNumber       -> Identificador
 */
data class Owner(
    val type: String,
    val name: String,
    val taxIdNumber: String
)

enum class TipoChaveBacen(val tipoChaveInterno: TipoChaveComValida?) {
    CPF(TipoChaveComValida.CPF),
    RANDOM(TipoChaveComValida.ALEATORIA),
    EMAIL(TipoChaveComValida.EMAIL),
    PHONE(TipoChaveComValida.TELEFONE_CELULAR);

    companion object {
        //Convertendo tipo de chave interno para tipo de chave do banco central
        private val mapping = values().associateBy(TipoChaveBacen::tipoChaveInterno)

        fun of(domainType: TipoChaveComValida): TipoChaveBacen {
            return mapping[domainType]
                ?: throw IllegalArgumentException("Tipo Chave pix para banco central invalida. [$domainType]")
        }
    }
}

enum class TipoContaBacen() {
    CACC,
    SVGS;

    companion object {
        //Convertendo tipo de conta interno para o valor do request do banco central
        fun of(tipoConta: TipoConta): TipoContaBacen {
            return when (tipoConta) {
                TipoConta.CONTA_CORRENTE -> CACC
                TipoConta.CONTA_POUPANCA -> SVGS
                else -> throw IllegalArgumentException("Tipo conta para banco central invalida. [$tipoConta]")
            }
        }
    }
}