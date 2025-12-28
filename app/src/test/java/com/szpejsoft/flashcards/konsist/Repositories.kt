package com.szpejsoft.flashcards.konsist

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.children
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test

class Repositories {

    @Test
    fun `repositories are defined in domain layer`() {
        Konsist.scopeFromProduction()
            .interfaces()
            .withNameEndingWith("Repository")
            .assertTrue { it.resideInPackage("..domain.repository..") }
    }

    @Test
    fun `repositories implementations are defined in data layer`() {
        Konsist.scopeFromProduction()
            .interfaces()
            .withNameEndingWith("Repository")
            .children()
            .assertTrue { it.resideInPackage("..data.repository..") }
    }

    @Test
    fun `there are the same number of repository implementations and interfaces`() {
        val implementations = Konsist.scopeFromProduction()
            .classes()
            .withNameEndingWith("RepositoryImpl")

        val interfaces = Konsist.scopeFromProduction()
            .interfaces()
            .withNameEndingWith("Repository")

        assertEquals(implementations.size, interfaces.size)
    }

    @Test
    fun `every interface has implementation`() {
        val repositories = Konsist.scopeFromProduction()
            .classes()
            .withNameEndingWith("RepositoryImpl")
            .map { it.name }
            .toTypedArray()

        val interfacesImplementations = Konsist.scopeFromProduction()
            .interfaces()
            .withNameEndingWith("Repository")
            .children(indirectChildren = false)
            .map { it.name }
            .toTypedArray()

        assertArrayEquals(interfacesImplementations, repositories)
    }

    @Test
    fun `repositories implementations have tests`() {
        Konsist.scopeFromProduction()
            .classes()
            .withNameEndingWith("RepositoryImpl")
            .assertTrue { it.hasTestClasses() }
    }

}