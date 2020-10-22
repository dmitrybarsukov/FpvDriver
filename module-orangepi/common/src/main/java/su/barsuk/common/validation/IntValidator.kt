package su.barsuk.common.validation

import kotlin.reflect.KProperty0

open class IntValidator(
        value: Int,
        name: String
) : NumberValidator<Int>(value, name) {
    constructor(property: KProperty0<Int>) : this(property.get(), property.name)
    constructor(value: Int) : this(value, UNNAMED)

    fun checkInRange(range: IntRange) {
        if(range.contains(value))
            return
        throw makeValidationException("must be in range [$range]")
    }

    fun checkOutOfRange(range: IntRange) {
        if(range.contains(value))
            throw makeValidationException("must be out of range [$range]")
    }
}