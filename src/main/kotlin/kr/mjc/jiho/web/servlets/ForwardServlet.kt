package kr.mjc.jiho.web.servlets

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

@WebServlet("/servlets/forward")
class ForwardServlet : HttpServlet() {
    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        // TODO: 어떤 처리를 한 후에 forward
        req.getRequestDispatcher("/servlets/template").forward(req, resp)//다른 앱으로는 불가
    }
}