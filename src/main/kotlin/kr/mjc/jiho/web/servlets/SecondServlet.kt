package kr.mjc.jiho.web.servlets

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.WebContext
import org.thymeleaf.web.servlet.JakartaServletWebApplication

@WebServlet("/servlets/second")
class SecondServlet: HttpServlet() {
    @Autowired
    lateinit var templateEngine: TemplateEngine

    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        log.debug("request scope : {}", req.getAttribute("title"))
        log.debug("session scope : {}", req.session.getAttribute("username"))
        log.debug("application scope : {}",
            servletContext.getAttribute("categories"))

        // webContext : request, session, application 영역을 context 넣음
        // Spring Web MVC에서 자동으로 처리함
        val webContext = WebContext(
            JakartaServletWebApplication.buildApplication(servletContext)
                .buildExchange(req, resp))

        resp.contentType = "text/html"
        templateEngine.process("examples/scope", webContext, resp.writer)
        //webContext에서 examples/scope로 맵핑?
    }
}