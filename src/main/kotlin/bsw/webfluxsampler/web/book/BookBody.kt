package bsw.webfluxsampler.web.book

object BookBody {
    data class Post(val bookName: String)
    data class Patch(val bookName: String)
}
