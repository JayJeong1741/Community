package kr.mjc.jiho.web.servlets

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

@WebServlet("/servlets/first")
class FirstServlet: HttpServlet() {
    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        // request 영역에 title 저장
        req.setAttribute("title", "This is title.")//request->한 번의 요청이 처리될 동안만 유효

        // session 영역에 username 저장
        req.session.setAttribute("username", "Jacob")//session -> 같은 세션에서만 유효

        // application 영역에 categories 저장
        servletContext.setAttribute("categories", listOf("액션", "서스펜스", "멜로"))
        //application -> 전역.(애플리케이션 전체에서 접근 가능)

        req.getRequestDispatcher("/servlets/second").forward(req, resp)
    }
}