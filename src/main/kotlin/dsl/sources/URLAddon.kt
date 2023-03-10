package io.github.frontrider.godle.addons.dsl.sources

import fi.linuxbox.gradle.download.Download
import io.github.frontrider.godle.addons.configureAsGodleInternal
import io.github.frontrider.godle.addons.dsl.AddonConfig
import io.github.frontrider.godle.addons.dsl.GodotAddon
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import java.io.File


/**
 * Downloads an addon from an url containing a zip file.
 * */
class URLAddon(val url: String, addonConfig: AddonConfig, project: Project) : GodotAddon(addonConfig, project) {

    override fun init() {
        val internalName = getAddonInternalName()
        val download = project.tasks.create("downloadGodotAddonFromURL$internalName", Download::class.java) {
            with(it) {
                configureAsGodleInternal("download addon $internalName")
                from(url)
                to(File(downloadFolder,"url_$internalName.zip"))
            }
        }

        val extract = project.tasks.create("extractGodotAddonFromURL$internalName", Copy::class.java){
            with(it){
                configureAsGodleInternal("extract addon $internalName")
                from(project.zipTree(File(downloadFolder,"url_$internalName.zip")))
                into(getLocalFolder())
                dependsOn(download)
            }
        }

        project.tasks.getByName(rootTaskName).dependsOn(extract)
    }

    override fun getAddonInternalName(): String {
        return url.replace("http://", "")
            .replace("https://", "")
            .replace("/", "_")
            .replace("#", "")
            .replace(Regex("\\?.*+"), "")
    }
}