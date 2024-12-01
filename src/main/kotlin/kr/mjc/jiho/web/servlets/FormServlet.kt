package kr.mjc.jiho.web.servlets

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

@WebServlet("/servlets/form")
class FormServlet : HttpServlet() {

    @Autowired
    lateinit var templateEngine: TemplateEngine

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        println("get processing...")
        val username = req.getParameter("username")
        val context = Context()
        context.setVariable("username", username)

        resp.contentType = "text/html"
        templateEngine.process("examples/form", context, resp.writer)
    }

    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        println("post processing...")
        val username = req.getParameter("username")
        val context = Context()
        context.setVariable("username", username)

        resp.contentType = "text/html"
        templateEngine.process("examples/form", context, resp.writer)
    }
}