package su.barsuk.common.extensions

import kotlin.math.max
import kotlin.math.min

fun Int.constrain(minimum: Int, maximum: Int): Int {
    return min(max(this, minimum), maximum)
}
