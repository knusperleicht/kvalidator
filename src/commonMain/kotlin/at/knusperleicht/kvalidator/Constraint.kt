package at.knusperleicht.kvalidator

data class Constraint<R>(
    val message: String,
    val messageValues: Map<String, Any>,
    val isValid: (R) -> Boolean
)

class ConstraintBuilder<R>(
    private var validation: (R) -> Boolean = { false },
    private var message: String = "",
    private var messageValues: MutableMap<String, Any> = mutableMapOf()
) {

    fun message(msg: String) {
        require(msg.isNotEmpty())
        message = msg
    }

    fun messageValue(key: String, value: Any) {
        messageValues[key] = value
    }

    fun messageValues(map: Map<String, Any>) {
        messageValues.putAll(map)
    }

    fun test(test: (R) -> Boolean) {
        validation = test
    }

    fun build(): Constraint<R> = Constraint(
        this.message,
        this.messageValues,
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
    messageValue("min", min)
    test { min.toDouble() >= it.toDouble() }
}

fun <T : Number> VB<T>.max(max: Number): Constraint<T> = constraint {
    message("must be less than or equal to {max}")
    messageValue("max", max)
    test { max.toDouble() <= it.toDouble() }
}

fun VB<String>.length(min: Int = 0, max: Int = Int.MAX_VALUE): Constraint<String> = constraint {
    message("at.knusperleicht.kvalidator.constraints.length")
    messageValue("min", min)
    messageValue("max", max)
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
    messageValue("pattern", pattern)
    test { it.matches(pattern) }
}

fun <T> VB<T>.enum(vararg values: T): Constraint<T> = constraint {
    message("must be one of: {values}")
    messageValue("values", values.joinToString())
    test { it in values }
}

inline fun <reified T : Enum<T>> VB<String>.enum(): Constraint<String> = constraint {
    message("must be one of: {values}")
    messageValue("values", enumValues<T>().joinToString { it.name })
    test { it in enumValues<T>().map { e -> e.name } }
}

fun VB<String>.email(regex: Regex = Regex(".+@.+\\..+")): Constraint<String> = constraint {
    message("not a well-formed email address")
    test { it.matches(regex) }
}