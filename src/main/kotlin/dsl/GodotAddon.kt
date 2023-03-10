package io.github.frontrider.godle.addons.dsl

import io.github.frontrider.godle.addons.configureAsGodleInternal
import io.github.frontrider.godle.addons.godleAddonsTaskName
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import java.io.File


/**
 * Declares a godot addon dependency
 * */
abstract class GodotAddon(val addonConfig: AddonConfig, val project: Project) {
    lateinit var targetFolder: String
    lateinit var downloadFolder: String
    lateinit var rootTaskName: String

    //a common root task that can depend on every other task.
    fun configure() {
        project.afterEvaluate {
            targetFolder = project.buildDir.absolutePath + "/godle/addons/${getAddonInternalName()}"
            downloadFolder = project.buildDir.absolutePath + "/godle/temp/addon/${getAddonInternalName()}"
            rootTaskName = godleAddonsTaskName + getAddonInternalName()
            //the addon folder must be initialized here.
            addonConfig.sourceFolder = File(targetFolder)

            val rootTask = project.tasks.create(rootTaskName) {
                it.configureAsGodleInternal()
            }

            project.tasks.getByName(godleAddonsTaskName).dependsOn(rootTask)

            init()
            addonConfig.init()
            createFolder(File(targetFolder))
            createFolder(File(downloadFolder))
            val internalName = getAddonInternalName()
            //This copy is the last task we do, everything else is set up to run before it.
            project.tasks.create("installGodotAddon${internalName}", Copy::class.java) {
                with(it) {
                    configureAsGodleInternal("install addon $internalName")
                    //uses the copyspec from the addon config.
                    with(addonConfig.copySpec)
                    destinationDir =File(project.rootDir, "/addons/")
                }
                rootTask.finalizedBy(it)
            }
        }
    }

    //this function is called to set up the dependency on the project.
    abstract fun init()

    /**
     * Returns the local folder where the addon's code can be found. It will be copied from there.
     */
    fun getLocalFolder(): File {
        return File(targetFolder)
    }

    fun createFolder(file: File) {
        if (!file.mkdirs()) {
            if (!file.exists()) {
                error("failed to create folder ${file.absolutePath}")
            }
        }
    }

    abstract fun getAddonInternalName(): String
}