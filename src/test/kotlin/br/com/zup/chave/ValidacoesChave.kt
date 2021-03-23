package br.com.zup.chave

import br.com.zup.chave.TipoChaveComValida
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class ValidacoesChave {

    //@Inject
    private val tipoDeChave: TipoChaveComValida = TipoChaveComValida.NAO_IDENTIFICADO

    @ParameterizedTest
    @MethodSource("geradorValoresValidadorCPF")
    fun `validacoes cpf`(cpf: String, esperado: Boolean) {
        Assert.assertEquals(esperado, TipoChaveComValida.CPF.valida(cpf))
    }

    @ParameterizedTest
    @MethodSource("geradorValoresValidadorEMAIL")
    fun `validacoes email`(email: String, esperado: Boolean) {
        Assert.assertEquals(esperado, TipoChaveComValida.EMAIL.valida(email))
    }

    @ParameterizedTest
    @MethodSource("geradorValoresValidadorTEL")
    fun `validacoes telefone celular`(tel: String, esperado: Boolean) {
        Assert.assertEquals(esperado, TipoChaveComValida.TELEFONE_CELULAR.valida(tel))
    }

    @Test
    fun `validacoes chave aleatoria`() {
        assertAll(
            { Assert.assertEquals(true, TipoChaveComValida.ALEATORIA.valida("")) },
            { Assert.assertEquals(false, TipoChaveComValida.ALEATORIA.valida("chaveNaoPermitida")) }
        )

    }

    companion object {
        @JvmStatic
        fun geradorValoresValidadorCPF() = listOf(
            Arguments.of("", false),
            Arguments.of("00000000000", false),
            Arguments.of("12312312300", false),
            Arguments.of("175.704.190-73", false),
            Arguments.of("17570419073", true)
        )

        @JvmStatic
        fun geradorValoresValidadorEMAIL() = listOf(
            Arguments.of("", false),
            Arguments.of("teste@email", false),
            Arguments.of("teste@email.com", true),
            Arguments.of("teste@email.com", true)
        )

        @JvmStatic
        fun geradorValoresValidadorTEL() = listOf(
            Arguments.of("", false),
            Arguments.of("+55", false),
            Arguments.of("+5511", false),
            Arguments.of("+5511123", false),
            Arguments.of("11999999999", false),
            Arguments.of("+11999999999", false),
            Arguments.of("+5511999999999", true)
        )
    }
}