class RepoResourceInMemoryCreateTest : RepoResourceCreateTest() {
    override val repo = RepoInitialized(
        RepoInMemory(randomUuid = { uuidNew.asString() }),
        initObjects = initObjects,
    )
}

class RepoResourceInMemoryDeleteTest : RepoResourceDeleteTest() {
    override val repo = RepoInitialized(
        RepoInMemory(),
        initObjects = initObjects,
    )
}

class RepoResourceInMemoryReadTest : RepoResourceReadTest() {
    override val repo = RepoInitialized(
        RepoInMemory(),
        initObjects = initObjects,
    )
}

class RepoResourceInMemorySearchTest : RepoResourceSearchTest() {
    override val repo = RepoInitialized(
        RepoInMemory(),
        initObjects = initObjects,
    )
}

class RepoResourceInMemoryUpdateTest : RepoResourceUpdateTest() {
    override val repo = RepoInitialized(
        RepoInMemory(),
        initObjects = initObjects,
    )
}
