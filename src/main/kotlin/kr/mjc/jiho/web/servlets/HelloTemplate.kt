package kr.mjc.jiho.web.servlets

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.awt.print.Book

@WebServlet("/servlets/template")
class HelloTemplate : HttpServlet() {
    @Autowired
    lateinit var templateEngine: TemplateEngine
    /*lateinit var bookRepository: JpaRepository<Book, Long>*/

    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse) {
        resp.contentType = "text/html"
        val html = templateEngine.process("examples/hello", Context(), resp.writer)
    }
}