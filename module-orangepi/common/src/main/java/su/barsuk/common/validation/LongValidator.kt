package su.barsuk.common.validation

import kotlin.reflect.KProperty0

open class LongValidator(
        value: Long,
        name: String
) : NumberValidator<Long>(value, name) {
    constructor(property: KProperty0<Long>) : this(property.get(), property.name)
    constructor(value: Long) : this(value, UNNAMED)

    fun checkInRange(range: LongRange) {
        if(range.contains(value))
            return
        throw makeValidationException("must be in range [$range]")
    }

    fun checkOutOfRange(range: LongRange) {
        if(range.contains(value))
            throw makeValidationException("must be out of range [$range]")
    }
}