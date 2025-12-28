package com.szpejsoft.flashcards.konsist

import androidx.lifecycle.ViewModel
import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.Test

class ViewModels {

    @Test
    fun `all viewModels are placed in proper package`() {
        Konsist.scopeFromProduction()
            .classes()
            .filter { it.hasParentOf(ViewModel::class) }
            .assertTrue { it.resideInPackage("..ui.screens..") }
    }

    @Test
    fun `all viewModels have tests`() {
        Konsist.scopeFromProduction()
            .classes()
            .filter { it.hasParentOf(ViewModel::class) }
            .assertTrue { it.hasTestClasses() }
    }

}