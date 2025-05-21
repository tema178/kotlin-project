import ResourceStubBook.RESOURCE_BOOK_AVAILABLE
import models.Resource

object ResourceStub {

    fun get(): Resource = RESOURCE_BOOK_AVAILABLE.copy()

    fun getList(): List<Resource> = listOf(get(), get())



}