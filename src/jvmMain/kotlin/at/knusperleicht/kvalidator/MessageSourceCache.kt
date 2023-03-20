package at.knusperleicht.kvalidator

import java.util.*
import java.util.concurrent.ConcurrentHashMap

private const val INITIAL_CACHE_SIZE = 50
private val messageCache: ConcurrentHashMap<String, Map<String, String>> = ConcurrentHashMap(INITIAL_CACHE_SIZE)

object MessageSourceCache {

    private var resourceBundleLocator: ResourceBundleLocator = ResourceBundleLocator()

    private val messages: Map<String, String> = messageCache.getOrPut(Locale.ENGLISH.country) {
        resourceBundleLocator.getResourceBundle(Locale.ENGLISH).let { bundle ->
            bundle.keySet()
                .asSequence()
                .map {
                    it to bundle.getString(it)
                }.toMap()
        }
    }

    fun message(key: String): String? = messages[key]
}