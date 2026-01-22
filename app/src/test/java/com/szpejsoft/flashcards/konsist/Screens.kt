package com.szpejsoft.flashcards.konsist

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.withAnnotation
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.Assert.assertTrue
import org.junit.Test

class Screens {

    @Test
    fun `compose _Screen methods reside in ui package`() {
        Konsist.scopeFromProduction()
            .functions()
            .withAnnotation { it.name == "Composable" }
            .withNameEndingWith("Screen")
            .assertTrue { it.resideInPackage("..ui.screens..") }
    }

    @Test
    fun `every screen has a test`() {
        val testClasses = Konsist.scopeFromTest()
            .classes()
            .map { it.name }

        val screens = Konsist.scopeFromProduction()
            .functions()
            .withAnnotation { it.name == "Composable" }
            .withNameEndingWith("Screen")
            .map { it.name }
            .filterNot { it == "MainScreen" }

        screens.forEach { screen ->
            assertTrue("$screen does not have a test class", testClasses.any { it == "${screen}Test" })
        }
    }

    @Test
    fun `every Screen subclass has a corresponding entry in ScreenNavigator entryProvider`() {

        val screenSubclasses = Konsist.scopeFromProduction()
            .classes()
            .first { it.name == "Screen" }
            .children()
            .map { it.name }

        // 2. Get the content of the ScreenNavigator file
        val navigatorFile = Konsist.scopeFromProduction()
            .files
            .first { it.name == "ScreenNavigator" }

        screenSubclasses.forEach { screenName ->
            assertTrue(
                "ScreenNavigator is missing entry<$screenName> in entryProvider",
                navigatorFile.text.contains("entry<$screenName>")
            )
        }
    }

}
