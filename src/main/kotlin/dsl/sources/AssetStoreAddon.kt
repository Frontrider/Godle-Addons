package io.github.frontrider.godle.addons.dsl.sources

import fi.linuxbox.gradle.download.Download
import godot.assets.api.AssetsApi
import godot.assets.invoker.ApiException
import io.github.frontrider.godle.addons.configureAsGodleInternal
import io.github.frontrider.godle.addons.dsl.AddonConfig
import io.github.frontrider.godle.addons.dsl.GodotAddon
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import java.io.File

/**
 * Downloads an addon from the godot asset library.
 * */
class AssetStoreAddon(val id: String, addonConfig: AddonConfig, project: Project) : GodotAddon(addonConfig, project) {
    override fun init() {
        val internalName = getAddonInternalName()
        try {
            val assetsApi = AssetsApi()
            val assetDetails = assetsApi.assetIdGet(id)
            //being gitlike is the default behavior, as most addons come from git repositories.

            addonConfig.isGitLike = assetDetails.downloadProvider!!.startsWith("Git")
            val download = project.tasks.create("downloadGodotAddonFromStore$internalName", Download::class.java) {
                with(it) {
                    configureAsGodleInternal("download $id from the asset library.")
                    from(assetDetails.downloadUrl)
                    to(File(downloadFolder, "store$internalName.zip"))
                }
            }

            val extract = project.tasks.create("extractGodotAddonFromStore$internalName", Copy::class.java) {
                with(it) {
                    configureAsGodleInternal("extract $id from the asset library")
                    from(project.zipTree(download.to.asFile))
                    into(getLocalFolder())
                    dependsOn(download)
                }
            }

            project.tasks.getByName(rootTaskName).dependsOn(extract)
        } catch (e: ApiException) {
            println("failed to resolve $id from godot asset library!")

            println(e.message)
            println("body:\n"+e.responseBody)
        }
    }

    override fun getAddonInternalName(): String {
        return "store-$id"
    }
}
