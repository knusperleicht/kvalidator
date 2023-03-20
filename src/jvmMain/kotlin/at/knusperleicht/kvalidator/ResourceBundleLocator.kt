package at.knusperleicht.kvalidator

import java.util.*

private const val DEFAULT_BASE_NAME = "at.knusperleicht.kvalidator.messages"

class ResourceBundleLocator() {

    fun getResourceBundle(locale: Locale): ResourceBundle {
       return ResourceBundle.getBundle(DEFAULT_BASE_NAME, locale, object : ResourceBundle.Control() {
            override fun getFormats(baseName: String?): List<String> = FORMAT_PROPERTIES
            override fun getFallbackLocale(baseName: String?, locale: Locale?): Locale? = locale
            override fun getCandidateLocales(baseName: String, locale: Locale): List<Locale> = listOf(locale)
        })

    }
}