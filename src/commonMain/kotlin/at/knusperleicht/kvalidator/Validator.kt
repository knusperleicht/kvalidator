package at.knusperleicht.kvalidator

import kotlin.reflect.KProperty1

object KValidate

typealias VB<T> = ValidatorBuilder<T>

@DslMarker
annotation class ValidationScope

interface Validator<T> {
    fun constraints(): List<Constraint<T>>
    fun validations(): List<Prop<T>>
}

@ValidationScope
class ValidatorBuilder<T>(
    private val constraints: MutableList<Constraint<T>> = mutableListOf(),
    private val validations: MutableList<Prop<T>> = mutableListOf()
) : Validator<T> {

    operator fun <R> KProperty1<T, R>.invoke(init: ValidatorBuilder<R>.() -> Unit) {
        val p = Property(this, ValidatorBuilder<R>().apply(init))
        validations.add(p)
    }

    infix fun Constraint<T>.message(msg: String): Constraint<T> = constraint {
        message(msg)
        test(isValid)
        messageValues(messageValues)
    }.also {
        constraints.remove(this)
    }

    fun addConstraint(constrain: Constraint<T>) {
        constraints.add(constrain)
    }

    fun build(): Validator<T> {
        return this
    }

    override fun constraints(): List<Constraint<T>> {
        return constraints.toMutableList()
    }

    override fun validations(): List<Prop<T>> {
        return validations.toMutableList()
    }
}

fun <T> validator(init: ValidatorBuilder<T>.() -> Unit): Validator<T> {
    val builder = ValidatorBuilder<T>()
    return builder.apply(init).build()
}



