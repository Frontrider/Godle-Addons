package io.github.frontrider.godle.addons

import io.github.frontrider.godle.addons.dsl.GodleAddonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
class GodleAddonPlugin :Plugin<Project>{
    override fun apply(target: Project) {
        target.extensions.create("godleAddons", GodleAddonExtension::class.java)
    }
}