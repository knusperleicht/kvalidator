package at.knusperleicht.kvalidator


actual class ResourceBundleMessageInterpolator {
    actual fun interpolate(
        messageKey: String,
        messageParameters: Map<String, Any>
    ): String? = messageParameters.asSequence().fold(MessageSourceCache.message(messageKey)) { message, entry ->
        message?.replace("{${entry.key}}", entry.value.toString())
    }
}