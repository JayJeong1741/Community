package kr.mjc.jiho.web.servlets

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

@WebServlet("/servlets/lifecycle")
class LifecycleServlet: HttpServlet() {

    override fun init() {
        println("Lifecycle 서블릿 초기화")
    }

    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse) {
        println("doGet() 실행")
        resp.contentType="text/html"
        resp.writer.println("<!DOCTYPE html><html><body><h1>Hello 서블릿.</h1></body></html>")
    }

    override fun destroy() {
        println("Destroyed")
    }
}