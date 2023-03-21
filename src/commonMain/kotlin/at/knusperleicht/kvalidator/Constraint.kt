package at.knusperleicht.kvalidator

data class Constraint<R>(
    val message: String,
    val parameters: Map<String, Any>,
    val isValid: (R) -> Boolean
)

class ConstraintBuilder<R>(
    private var validation: (R) -> Boolean = { false },
    private var message: String = "",
    private var parameters: MutableMap<String, Any> = mutableMapOf()
) {

    fun message(msg: String) {
        require(msg.isNotEmpty())
        message = msg
    }

    fun parameter(key: String, value: Any) {
        parameters[key] = value
    }

    fun parameters(vararg pairs: Pair<String, Any>) {
        parameters.putAll(pairs)
    }

    fun test(test: (R) -> Boolean) {
        validation = test
    }

    fun build(): Constraint<R> = Constraint(
        this.message,
        this.parameters,
        this.validation
    )
}

fun <R> ValidatorBuilder<R>.constraint(init: ConstraintBuilder<R>.() -> Unit): Constraint<R> {
    val constraintBuilder = ConstraintBuilder<R>()
    val constraint = constraintBuilder.apply(init).build()
    this.addConstraint(constraint)
    return constraint
}

fun <T : Number> VB<T>.min(min: Number): Constraint<T> = constraint {
    message("must be greater than or equal to {min}")
    parameter("min", min)
    test { min.toDouble() >= it.toDouble() }
}

fun <T : Number> VB<T>.max(max: Number): Constraint<T> = constraint {
    message("must be less than or equal to {max}")
    parameter("max", max)
    test { max.toDouble() <= it.toDouble() }
}

fun VB<String>.length(min: Int = 0, max: Int = Int.MAX_VALUE): Constraint<String> = constraint {
    message("at.knusperleicht.kvalidator.constraints.length")
    parameters(
        "min" to min,
        "max" to max
    )
    test {
        val length: Int = it.length
        length in min..max
    }
}

fun VB<String>.notBlank(): Constraint<String> = constraint {
    message("must not be blank")
    test { it.isNotBlank() }
}

fun VB<String>.pattern(pattern: Regex): Constraint<String> = constraint {
    message("must must match exact pattern {pattern}")
    parameter("pattern", pattern)
    test { it.matches(pattern) }
}

fun <T> VB<T>.enum(vararg values: T): Constraint<T> = constraint {
    message("must be one of: {values}")
    parameter("values", values.joinToString())
    test { it in values }
}

inline fun <reified T : Enum<T>> VB<String>.enum(): Constraint<String> = constraint {
    message("must be one of: {values}")
    parameter("values", enumValues<T>().joinToString { it.name })
    test { it in enumValues<T>().map { e -> e.name } }
}

internal const val EMAIL_REGEX = ".+@.+\\..+"
fun VB<String>.email(regex: Regex = Regex(EMAIL_REGEX)): Constraint<String> = constraint {
    message("not a well-formed email address")
    test { it.matches(regex) }
}

fun <T> VB<T>.match(match: T.() -> Boolean): Constraint<T> = constraint {
    message("values must match")
    test { it.match() }
}

fun <T> VB<T>.inList(list: Iterable<T>): Constraint<T> = constraint {
    message("must be one of: {values}")
    parameter("values", list.joinToString { it.toString() })
    test { it in list }
}