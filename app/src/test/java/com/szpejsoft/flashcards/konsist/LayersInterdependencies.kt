package com.szpejsoft.flashcards.konsist

import com.lemonappdev.konsist.api.Konsist.scopeFromPackage
import com.lemonappdev.konsist.api.verify.assertFalse
import org.junit.Test

class LayersInterdependencies {

    @Test
    fun `all files in ui package don't have imports from data package`() {
        scopeFromPackage(UI_PACKAGE_SCOPE, "app")
            .files
            .assertFalse { it.hasImport { import -> import.name.contains(DATA_PACKAGE) } }
    }

    @Test
    fun `all files in domain package don't have imports from data package`() {
        scopeFromPackage(DOMAIN_PACKAGE_SCOPE, "app")
            .files
            .assertFalse { it.hasImport { import -> import.name.contains(DATA_PACKAGE) } }
    }

    @Test
    fun `all files in domain package don't have imports from ui package`() {
        scopeFromPackage(DOMAIN_PACKAGE_SCOPE, "app")
            .files
            .assertFalse { it.hasImport { import -> import.name.contains(UI_PACKAGE) } }
    }

    @Test
    fun `all files in data package don't have imports from ui package`() {
        scopeFromPackage(DATA_PACKAGE_SCOPE, "app")
            .files
            .assertFalse { it.hasImport { import -> import.name.contains(UI_PACKAGE) } }
    }

    companion object {
        const val DATA_PACKAGE = "com.szpejsoft.flashcards.data."
        const val DATA_PACKAGE_SCOPE = "$DATA_PACKAGE."
        const val DOMAIN_PACKAGE = "com.szpejsoft.flashcards.domain."
        const val DOMAIN_PACKAGE_SCOPE = "$DOMAIN_PACKAGE."
        const val UI_PACKAGE = "com.szpejsoft.flashcards.ui."
        const val UI_PACKAGE_SCOPE = "$UI_PACKAGE."
    }

}