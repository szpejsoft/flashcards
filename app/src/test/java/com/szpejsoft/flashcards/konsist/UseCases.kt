package com.szpejsoft.flashcards.konsist

import com.lemonappdev.konsist.api.KoModifier
import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.functions
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.Test

class UseCases {

    @Test
    fun `all usecases are placed in proper package`() {
        Konsist.scopeFromProduction()
            .classes()
            .withNameEndingWith("UseCase")
            .assertTrue { it.resideInPackage("..domain.usecase..") }
    }

    @Test
    fun `all usecases have only one method`() {
        Konsist.scopeFromProduction()
            .classes()
            .withNameEndingWith("UseCase")
            .assertTrue { it.functions().size == 1 }
    }

    @Test
    fun `all usecases methods are 'invoke'`() {
        Konsist.scopeFromProduction()
            .classes()
            .withNameEndingWith("UseCase")
            .functions()
            .assertTrue { it.name == "invoke" }
    }

    @Test
    fun `all usecases methods are operator methods`() {
        Konsist.scopeFromProduction()
            .classes()
            .withNameEndingWith("UseCase")
            .functions()
            .assertTrue { it.modifiers.contains(KoModifier.OPERATOR) }
    }

    @Test
    fun `all usecases have tests`() {
        Konsist.scopeFromProduction()
            .classes()
            .withNameEndingWith("UseCase")
            .assertTrue { it.hasTestClasses() }
    }
}