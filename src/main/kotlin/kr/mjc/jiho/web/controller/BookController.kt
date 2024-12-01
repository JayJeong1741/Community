package kr.mjc.jiho.web.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.mjc.jiho.web.repository.book.Book
import kr.mjc.jiho.web.repository.book.BookRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

//@Controller
class BookController(val bookRepository: BookRepository) {
    companion object {
        private const val PAGE_SIZE: Int = 100
        private val sort = Sort.by("id").descending()
        private const val LANDING_PAGE = "/book/list"
    }
    @GetMapping("/book/list")
    fun bookList(req: HttpServletRequest, model: Model) {
        val page = req.getParameter("page")?.toInt() ?: 0
        val book = bookRepository.findAll(PageRequest.of(page, PAGE_SIZE, sort))
        model.addAttribute("books", book)
    }
    @GetMapping("/book/save")
    fun bookSave(req: HttpServletRequest, resp: HttpServletResponse){
        val title = req.getParameter("title")
        val author = req.getParameter("author")
        val book = Book().apply {
            this.title = title
            this.author = author
        }
        bookRepository.save(book)
        resp.sendRedirect("${req.contextPath}/book/list")
    }
}