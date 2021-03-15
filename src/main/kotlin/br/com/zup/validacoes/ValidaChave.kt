package br.com.zup.validacoes

import br.com.zup.chave.cadastro.NovaChavePix
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import javax.inject.Singleton
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.TYPE
import kotlin.reflect.KClass

/**
 * Anotacao para validar tipo de chave (CPF, EMAIL, TELEFONE, ALEATORIA...)
 */
@MustBeDocumented
@Target(CLASS, TYPE)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidaChaveValidator::class])
annotation class ValidaChave(
    val message: String = "Chave pix inválida para o respectivo tipo.",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
)

/**
 * Validador da anotacao acima.
 */
@Singleton
class ValidaChaveValidator : ConstraintValidator<ValidaChave, NovaChavePix> {
    override fun isValid(
        value: NovaChavePix?,
        annotationMetadata: AnnotationValue<ValidaChave>,
        context: ConstraintValidatorContext
    ): Boolean {
        value?.tipoChave ?: return false
        return value.tipoChave.valida(value.chave)
    }

}
