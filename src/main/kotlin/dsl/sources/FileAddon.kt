package io.github.frontrider.godle.addons.dsl.sources
import io.github.frontrider.godle.addons.dsl.AddonConfig
import io.github.frontrider.godle.addons.dsl.GodotAddon
import org.gradle.api.Project
import java.io.File

/**
 * This addon takes in the path to the addon's folder, then uses it as a target. No downloads included, just a copy..
 * */
class FileAddon(val file: File, addonConfig: AddonConfig, project: Project) : GodotAddon(addonConfig, project)  {

    init {
        targetFolder = file.absolutePath
    }

    override fun init() {

    }

    override fun getAddonInternalName(): String {
        return file.absolutePath.toString().replace("/","_").replace("\\","").replace(":","_")
    }
}