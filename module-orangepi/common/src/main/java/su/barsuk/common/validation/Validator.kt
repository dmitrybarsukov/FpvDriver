package su.barsuk.common.validation

import kotlin.reflect.KProperty0

open class Validator<T> (
        protected val value: T,
        protected val name: String
) {
    constructor(property: KProperty0<T>) : this(property.get(), property.name)
    constructor(value: T) : this(value, UNNAMED)

    protected fun makeValidationException(requirement: String): ValidationException {
        return ValidationException("$name=$value $requirement")
    }

    protected companion object {
        const val UNNAMED = "<Unnamed>"
    }
}