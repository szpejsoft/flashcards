package com.szpejsoft.flashcards.konsist

import androidx.lifecycle.ViewModel
import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ViewModels {

    @Test
    fun `all viewModels are placed in proper package`() {
        Konsist.scopeFromProduction()
            .classes()
            .filter { it.hasParentOf(ViewModel::class) }
            .assertTrue { it.resideInPackage("..presentation..") }
    }

    @Test
    fun `there are the same number of viewModel implementations and interfaces`() {
        val implementations = Konsist.scopeFromProduction()
            .classes()
            .filter { it.hasParentOf(ViewModel::class) }

        val interfaces = Konsist.scopeFromProduction()
            .interfaces()
            .withNameEndingWith("ViewModel")

        assertEquals(implementations.size, interfaces.size)
    }

    @Test
    fun `every viewModel interface has implementation`() {
        val implementations = Konsist.scopeFromProduction()
            .classes()
            .filter { it.resideInPackage("..presentation..") }
            .filter { it.hasParentOf(ViewModel::class) }
            .map { it.name }
            .toTypedArray()

        val interfaces = Konsist.scopeFromProduction()
            .interfaces(includeNested = false)
            .withNameEndingWith("ViewModel")
            .filter { it.resideInPackage("..presentation..") }
            .map { it.name }
            .toTypedArray()

        implementations.forEach {
            assertTrue("$it doesn't implement known VM interface", interfaces.contains(it.removeSuffix("Impl")))
        }
    }

    @Test
    fun `all viewModels have tests`() {
        Konsist.scopeFromProduction()
            .classes()
            .filter { it.hasParentOf(ViewModel::class) }
            .assertTrue { it.hasTestClasses() }
    }

}
