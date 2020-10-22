package su.barsuk.common.validation

import kotlin.reflect.KProperty0

open class NumberValidator<T: Comparable<T>>(
        value: T,
        name: String
) : Validator<T>(value, name) {
    constructor(property: KProperty0<T>) : this(property.get(), property.name)
    constructor(value: T) : this(value, UNNAMED)

    fun checkGreaterThan(minimum: T) {
        if(value > minimum)
            return
        throw makeValidationException("must be greater than $minimum")
    }

    fun checkGreaterOrEqualThan(minimum: T) {
        if(value > minimum)
            return
        throw makeValidationException("must be greater or equal than $minimum")
    }

    fun checkLessThan(maximum: T) {
        if(value < maximum)
            return
        throw makeValidationException("must be less than $maximum")
    }

    fun checkLessOrEqualThan(maximum: T) {
        if(value < maximum)
            return
        throw makeValidationException("must be less or equal than $maximum")
    }
}