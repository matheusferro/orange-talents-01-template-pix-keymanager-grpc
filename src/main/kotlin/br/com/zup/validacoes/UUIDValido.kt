package br.com.zup.validacoes

import javax.validation.Constraint
import javax.validation.Payload
import javax.validation.ReportAsSingleViolation
import javax.validation.constraints.Pattern
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

/**
 * Validação para UUID po regex, implementada após ver solução.
 */
@ReportAsSingleViolation
@Target(FIELD, VALUE_PARAMETER, CONSTRUCTOR, PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [])
@Pattern(
    regexp = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}",
    flags = [Pattern.Flag.CASE_INSENSITIVE]
)
annotation class UUIDValido(
    val message: String = "UUID deve ter um formato válido.",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)
