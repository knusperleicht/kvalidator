package at.knusperleicht.kvalidator

import kotlin.reflect.KProperty1


interface Prop<T> {
    fun validate(value: T): MutableList<ConstraintViolation>
}

data class Property<T, R>(
    val key: KProperty1<T, R>,
    val builder: Validator<R>
) : Prop<T> {
    override fun validate(value: T): MutableList<ConstraintViolation> {
        val data = key(value)
        println(data)
        return builder.validate(data)
    }
}

data class PropertyArray<T, R>(
    val key: KProperty1<T, List<R>>,
    val builder: Validator<R>
) : Prop<T> {
    override fun validate(value: T): MutableList<ConstraintViolation> {
        val data = key(value)
        println(data)
        return  mutableListOf()
    }
}

private val mv = ResourceBundleMessageInterpolator()

fun <T> Validator<T>.validate(value: T, failFast: Boolean = false): MutableList<ConstraintViolation> {
    val violations = mutableListOf<ConstraintViolation>()

    loop@ for (it in validations()) {
        violations.addAll(it.validate(value))
        if (failFast && violations.size >= 1) {
            break@loop
        }
    }


    constraints()
        .filter { !it.isValid(value) }
        .forEach {
            val msg = mv.interpolate(it.message, it.parameters)
            violations.add(ConstraintViolation(msg.orEmpty()))
        }
    return violations
}