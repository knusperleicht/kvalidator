package at.knusperleicht.kvalidator

expect class ResourceBundleMessageInterpolator() {

     fun interpolate(messageKey: String, messageParameters: Map<String, Any>) : String ?
}