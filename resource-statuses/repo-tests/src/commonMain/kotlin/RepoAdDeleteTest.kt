package ru.otus.otuskotlin.marketplace.backend.repo.tests

import BaseInitResources
import models.*
import repo.DbResponseErr
import repo.DbIdRequest
import repo.DbResponseOk
import repo.IRepo
import runRepoTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

abstract class RepoAdDeleteTest {
    abstract val repo: IRepo
    protected open val deleteSucc = initObjects[0]
    protected open val notFoundId = ResourceId("ad-repo-delete-notFound")

    @Test
    fun deleteSuccess() = runRepoTest {
        val result = repo.delete(DbIdRequest(deleteSucc.id))
        assertIs<DbResponseOk>(result)
        assertEquals(deleteSucc.type, result.data.type)
        assertEquals(deleteSucc.status, result.data.status)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.read(DbIdRequest(notFoundId))

        assertIs<DbResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertNotNull(error)
    }

    companion object : BaseInitResources("delete") {
        override val initObjects: List<Resource> = listOf(
            createInitTestModel("delete"),
        )
    }
}
