package kr.mjc.jiho.web.controller

import kr.mjc.jiho.web.repository.book.Book
import kr.mjc.jiho.web.repository.book.BookRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class BookControllerV2(val bookRepository: BookRepository) {
    companion object{
        private const val PAGE_SIZE = 20
    }
    @GetMapping("/book/list")
    fun listBook(@RequestParam page : Int=0 , model: Model){
        val books : Slice<Book> =
            bookRepository.findAllByOrderByIdDesc(PageRequest.of(page, PAGE_SIZE))
        model.addAttribute("list", books)
    }
    @PostMapping("/book/save")
    fun saveBook(@ModelAttribute book:Book) : String{
        bookRepository.save(book)
        return "redirect:/book/list"
    }
}