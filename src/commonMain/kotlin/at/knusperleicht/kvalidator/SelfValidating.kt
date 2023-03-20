package at.knusperleicht.kvalidator

abstract class SelfValidating<T> {
    protected abstract fun validator(): Validator<T>

    fun validateSelf(failFast: Boolean = false): MutableList<ConstraintViolation> =
        validator().validate(this as T, failFast)

    fun isValid(failFast: Boolean = false): Boolean = validateSelf(failFast).isEmpty()

    fun isInvalid(failFast: Boolean = false): Boolean = validateSelf(failFast).isNotEmpty()
}