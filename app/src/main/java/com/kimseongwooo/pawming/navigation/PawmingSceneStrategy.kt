package com.kimseongwooo.pawming.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope

internal object TopLevelKey : NavMetadataKey<Boolean>

internal fun topLevelMetadata(): Map<String, Any> = mapOf(TopLevelKey.toString() to true)

internal class PawmingSceneStrategy(
    private val bottomBar: @Composable () -> Unit
) : SceneStrategy<NavKey> {

    override fun SceneStrategyScope<NavKey>.calculateScene(
        entries: List<NavEntry<NavKey>>
    ): Scene<NavKey> {
        val lastEntry = entries.last()
        val prevEntries = entries.dropLast(1)
        val isTopLevel = lastEntry.metadata.containsKey(TopLevelKey.toString())
        val capturedBottomBar = bottomBar

        return object : Scene<NavKey> {
            override val key: Any = lastEntry.contentKey
            override val entries: List<NavEntry<NavKey>> = listOf(lastEntry)
            override val previousEntries: List<NavEntry<NavKey>> = prevEntries
            override val content: @Composable () -> Unit = {
                Scaffold(bottomBar = { if (isTopLevel) capturedBottomBar() }) { innerPadding ->
                    Box(Modifier.padding(innerPadding)) { lastEntry.Content() }
                }
            }
        }
    }
}
