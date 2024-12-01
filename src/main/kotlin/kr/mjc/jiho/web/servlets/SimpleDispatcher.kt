package kr.mjc.jiho.web.servlets

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.mjc.jiho.web.webContext
import org.hibernate.annotations.Target
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.stereotype.Controller
import org.thymeleaf.TemplateEngine

@WebServlet("/mvc/*")
class SimpleDispatcher : HttpServlet() {

    @Autowired lateinit var requestHandler: RequestHandler
    @Autowired lateinit var templateEngine: TemplateEngine

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        println("servletPath = ${req.servletPath}") // 이 서블릿의 path: /mvc
        println("pathInfo = ${req.pathInfo}") // /mvc 뒤의 uri

        val context = webContext(req, resp)
        when (req.pathInfo) {//분기추가
           /**User**/

            "/user/list" -> requestHandler.userList(req, context)
            "/user/detail" -> requestHandler.userDetail(req, context)
            "/user/profile" -> requestHandler.userProfile(req,context)

            /**Post**/

            "/post/list" -> requestHandler.postList(req,context)
            "/post/detail" -> requestHandler.postDetail(req, context)
            "/post/update" -> requestHandler.postUpdate(req, context)
         }
        val template = req.pathInfo.substring(1)  // '/' 제거
        resp.contentType = "text/html"
        templateEngine.process(template, context, resp.writer)
    }

    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        when(req.pathInfo){
            /**User**/
            "/user/login" -> requestHandler.userLogin(req, resp)
            "/user/logout" -> requestHandler.userLogout(req,resp)

            /**Post**/
            "/post/create" -> requestHandler.postCreate(req, resp)
            "/post/update" -> requestHandler.updatePost(req, resp)
            "/post/delete" -> requestHandler.deletePost(req, resp)
        }
    }
}
/*
*만든 dispatcher에는 filter를 사용해야함.
* Spring Web MVC
* Model : Entity, Repository
* View : Thymeleaf template, html, css, js
* Controller : dispatcher, requestHandler
*
* --> Dispatcher는 스프링이 제공
*
* @Component
* 1.Repository : JpaRepository extends하면 쓸일 없음
* 2.Service : 복잡한 비즈니스 로직이 있는 서비스
* 3.Controller : 요청을 처리
* */