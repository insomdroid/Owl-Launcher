/*
 * Minimal icon pack support. A pack is an installed app that declares one of the common
 * icon-pack intents and ships res/xml/appfilter.xml mapping components to drawables.
 */
package com.android.launcher3.owl

import android.content.ComponentName
import android.content.Context
import com.android.launcher3.LauncherAppState
import com.android.launcher3.LauncherPrefs
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.Xml
import org.xmlpull.v1.XmlPullParser

object OwlIconPacks {

    /**
     * Stores the choice and drops cached icons. Home and All Apps reload with the new pack,
     * without restarting the launcher.
     */
    fun select(context: Context, pkg: String) {
        LauncherPrefs.get(context).put(LauncherPrefs.OWL_ICON_PACK to pkg)
        clearCache()
        val app = LauncherAppState.getInstance(context)
        app.iconCache.invalidateAllIcons {
            android.os.Handler(android.os.Looper.getMainLooper()).post { app.model.forceReload() }
        }
    }


    private val PACK_INTENTS = listOf(
        "org.adw.launcher.THEMES",
        "com.novalauncher.THEME",
        "com.anddoes.launcher.THEME",
        "com.gau.go.launcherex.theme",
    )

    /** Package names of installed icon packs, sorted by label. */
    fun installedPacks(context: Context): List<String> {
        val pm = context.packageManager
        return PACK_INTENTS
            .flatMap { action -> pm.queryIntentActivities(Intent(action), 0) }
            .map { it.activityInfo.packageName }
            .distinct()
            .sortedBy { pkg -> labelOf(context, pkg) }
    }

    fun labelOf(context: Context, packageName: String): String = try {
        context.packageManager.getApplicationLabel(
            context.packageManager.getApplicationInfo(packageName, 0)).toString()
    } catch (e: PackageManager.NameNotFoundException) {
        packageName
    }

    /** How apps outside the pack are drawn: automatic, original or monochrome. */
    fun unthemedMode(context: Context): String = LauncherPrefs.get(context).get(LauncherPrefs.OWL_UNTHEMED_MODE)

    /** The package of the selected icon pack, or empty for none. */
    fun selected(context: Context): String = LauncherPrefs.get(context).get(LauncherPrefs.OWL_ICON_PACK)

    /** Parsed appfilter for one pack: component to drawable name, plus iconback names. */
    private class PackData(val mapping: Map<ComponentName, String>, val iconBacks: List<String>)

    private val packCache = HashMap<String, PackData?>()

    /** Drops parsed pack data so a changed pack is read again. */
    @Synchronized
    fun clearCache() = packCache.clear()

    /**
     * Icon from the selected pack for [component], or null to use the default icon.
     * When the pack declares iconback artwork, the icon is placed on one of those backplates,
     * the same way Nova and Nomina do it.
     */
    fun iconFor(context: Context, component: ComponentName, dpi: Int): Drawable? {
        val pack = selected(context)
        if (pack.isEmpty()) return null
        return try {
            val data = packData(context, pack) ?: return null
            val drawableName = data.mapping[component] ?: return null
            val packContext = context.createPackageContext(pack, 0)
            val res = packContext.resources
            val id = res.getIdentifier(drawableName, "drawable", pack)
            if (id == 0) return null
            val icon = res.getDrawableForDensity(id, dpi, null) ?: return null
            icon
        } catch (e: Exception) {
            android.util.Log.w("OwlIconPacks", "Icon pack $pack failed for $component", e)
            null
        }
    }

    @Synchronized
    private fun packData(context: Context, pack: String): PackData? {
        if (packCache.containsKey(pack)) return packCache[pack]
        val packContext = context.createPackageContext(pack, 0)
        val data = readAppFilter(packContext)
        packCache[pack] = data
        return data
    }

    private fun readAppFilter(packContext: Context): PackData? {
        val res = packContext.resources
        val map = HashMap<ComponentName, String>()
        val backs = LinkedHashSet<String>()
        val xmlId = res.getIdentifier("appfilter", "xml", packContext.packageName)
        val parser: XmlPullParser = if (xmlId != 0) {
            res.getXml(xmlId)
        } else {
            val assetParser = android.util.Xml.newPullParser()
            try {
                packContext.assets.open("appfilter.xml").use { input ->
                    assetParser.setInput(input, "utf-8")
                }
            } catch (e: java.io.IOException) {
                return null
            }
            assetParser
        }
        try {
            var event = parser.eventType
            while (event != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG) {
                    when (parser.name) {
                        "item" -> {
                            val component = parser.getAttributeValue(null, "component")
                            val drawable = parser.getAttributeValue(null, "drawable")
                            if (component != null && drawable != null) {
                                // Format: ComponentInfo{package/class}
                                val inner = component.removePrefix("ComponentInfo{").removeSuffix("}")
                                val name: ComponentName? = ComponentName.unflattenFromString(inner)
                                if (name != null && name !in map) map[name] = drawable
                            }
                        }
                        "iconback" -> {
                            for (index in 0 until parser.attributeCount) {
                                if (parser.getAttributeName(index).startsWith("img")) {
                                    parser.getAttributeValue(index)?.takeIf { it.isNotBlank() }?.let(backs::add)
                                }
                            }
                        }
                    }
                }
                event = parser.next()
            }
        } finally {
            if (xmlId != 0) (parser as? AutoCloseable)?.close()
        }
        return PackData(map, backs.toList())
    }
}
