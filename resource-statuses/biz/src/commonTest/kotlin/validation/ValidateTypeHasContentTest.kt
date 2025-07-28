package validation

import Context
import kotlinx.coroutines.test.runTest
import models.Filter
import models.Resource
import models.ResourceType
import models.State
import rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateTypeHasContentTest {
    @Test
    fun emptyString() = runTest {
        val ctx = Context(state = State.RUNNING, validating = Resource(type = ResourceType("")))
        chain.exec(ctx)
        assertEquals(State.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun noContent() = runTest {
        val ctx = Context(state = State.RUNNING, validating = Resource(type = ResourceType("12!@#$%^&*()_+-=")))
        chain.exec(ctx)
        assertEquals(State.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-type-noContent", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = Context(state = State.RUNNING, filterValidating = Filter(type = ResourceType("Ж")))
        chain.exec(ctx)
        assertEquals(State.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    companion object {
        val chain = rootChain {
            validateTypeHasContent("")
        }.build()
    }
}
