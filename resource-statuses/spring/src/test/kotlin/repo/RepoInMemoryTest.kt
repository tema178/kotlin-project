package org.tema.app.spring.repo

import RepoInMemory
import RepoInitialized
import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.slot
import models.ResourceType
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.tema.app.spring.config.Config
import org.tema.app.spring.controllers.StatusController
import repo.DbFilterRequest
import repo.DbIdRequest
import repo.DbRequest
import repo.IRepo
import kotlin.test.Test

// Temporary simple test with stubs
@WebFluxTest(StatusController::class, Config::class)
internal class RepoInMemoryTest : RepoBaseTest() {
    @Autowired
    override lateinit var webClient: WebTestClient

    @MockkBean
    @Qualifier("testRepo")
    lateinit var testTestRepo: IRepo

    @BeforeEach
    fun tearUp() {
        val slot = slot<DbRequest>()
        val slotId = slot<DbIdRequest>()
        val slotFl = slot<DbFilterRequest>()
        val repo = RepoInitialized(
            repo = RepoInMemory(randomUuid = { uuidNew }),
            initObjects = ResourceStub.prepareSearchList(ResourceType("xx")) + ResourceStub.get()
        )
        coEvery { testTestRepo.create(capture(slot)) } coAnswers { repo.create(slot.captured) }
        coEvery { testTestRepo.read(capture(slotId)) } coAnswers { repo.read(slotId.captured) }
        coEvery { testTestRepo.update(capture(slot)) } coAnswers { repo.update(slot.captured) }
        coEvery { testTestRepo.delete(capture(slotId)) } coAnswers { repo.delete(slotId.captured) }
        coEvery { testTestRepo.search(capture(slotFl)) } coAnswers { repo.search(slotFl.captured) }
    }

    @Test
    override fun create() = super.create()

    @Test
    override fun read() = super.read()

    @Test
    override fun update() = super.update()

    @Test
    override fun delete() = super.delete()

    @Test
    override fun search() = super.search()
}
