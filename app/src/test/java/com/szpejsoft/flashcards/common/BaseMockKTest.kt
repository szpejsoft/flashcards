package com.szpejsoft.flashcards.common

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before

abstract class BaseMockKTest {
    @Before
    fun baseMockKSetUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun baseMockKTearDown() {
        unmockkAll()
    }
}
