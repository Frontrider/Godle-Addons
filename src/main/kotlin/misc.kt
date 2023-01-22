package io.github.frontrider.godle.addons

import org.gradle.api.Task

const val godleAddonsTaskName = "installGodotAddons"
internal fun Task.configureAsGodleInternal(description:String="godle addons internalTask"){
    this.description = description
    group = "godle internal"
}
