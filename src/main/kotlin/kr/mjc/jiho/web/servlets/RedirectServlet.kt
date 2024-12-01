package kr.mjc.jiho.web.servlets

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

@WebServlet("/servlets/redirect")
class RedirectServlet : HttpServlet() {
    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        // TODO: 어떤 처리를 한 후에 redirect
        resp.sendRedirect("${req.contextPath}/servlets/template")//다른 앱으로 가능
        /*("/~~") 형식으로 쓰면 루트콘텍스트, 하드코딩하면 안좋음. 지금 작성한 방식으로 사용
        * 하드코드버전 : "app1/servlets/template"*/
    }
}