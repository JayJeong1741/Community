package kr.mjc.jiho.web.servlets

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.mjc.jiho.web.repository.post.Post
import kr.mjc.jiho.web.repository.post.PostRepository
import kr.mjc.jiho.web.repository.user.User
import kr.mjc.jiho.web.repository.user.UserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import org.thymeleaf.context.WebContext
import java.time.LocalDateTime

@Component
class RequestHandler(private val userRepository: UserRepository, private val postRepository: PostRepository,
                     val passwordEncoder: PasswordEncoder
) {
    companion object{
        private const val LANDING_PAGE = "/user/list"
    }


    val sort = Sort.by("id").descending()

    fun userLogin(req: HttpServletRequest, resp : HttpServletResponse){
        val username = req.getParameter("username")
        val password = req.getParameter("password")

        val user = userRepository.findByUsername(username)
        if(user != null && passwordEncoder.matches(password, user.password)){
            req.session.setAttribute("user", user)
            user.lastLogin = LocalDateTime.now()
            userRepository.updateLastLogin(user.id, user.lastLogin)
            resp.sendRedirect("${req.contextPath}/mvc/user/list")
        } else {  // 사용자가 없거나 비밀번호가 매치하지 않을 경우
            resp.sendRedirect("${req.contextPath}/user/login?error")
        }
    }
    fun userLogout(req: HttpServletRequest, resp: HttpServletResponse){
        req.session.invalidate()
        resp.sendRedirect("${req.contextPath}/user/list")
    }
    fun userProfile(req: HttpServletRequest, context: WebContext){

    }

    //어노테이션
    fun userList(req: HttpServletRequest, context: WebContext) {
        val page = req.getParameter("page")?.toInt() ?: 0
        val users = userRepository.findAll(PageRequest.of(page, 50, sort))
        context.setVariable("users", users)
    }

    fun userDetail(req: HttpServletRequest, context: WebContext) {
        val id = req.getParameter("id").toLong()
        val user = userRepository.findById(id).orElseThrow()
        context.setVariable("user", user)
    }

    fun postList(req: HttpServletRequest, context: WebContext){
        val page = req.getParameter("page")?.toInt() ?: 0
        val posts = postRepository.findAll(PageRequest.of(page, 50, sort))
        context.setVariable("list", posts)
    }
    fun postDetail(req: HttpServletRequest, context: WebContext){
        val id = req.getParameter("id").toLong()
        val post = postRepository.findById(id).orElseThrow()
        context.setVariable("post", post)
    }
    fun postCreate(req: HttpServletRequest, resp: HttpServletResponse){
        val title = req.getParameter("title")
        val content = req.getParameter("content")
        val user = req.session.getAttribute("user") as User

        val post = Post().apply {
            this.user = user
            this.title = title
            this.content = content
            this.pubDate = LocalDateTime.now()
            this.lastModified = LocalDateTime.now()
        }
        postRepository.save(post)
        resp.sendRedirect("${req.contextPath}/mvc/post/list")
    }
    fun updatePost(req: HttpServletRequest, resp: HttpServletResponse){
        val id = req.getParameter("id").toLong()
        val user = req.session.getAttribute("user") as User
        val post = checkPost(id, user.id)

        post.apply {
            post.title = req.getParameter("title")
            post.content = req.getParameter("content")
            post.lastModified = LocalDateTime.now()
        }
        postRepository.save(post)
        resp.sendRedirect("${req.contextPath}/mvc/post/detail?id=${post.id}")
    }

    fun postUpdate(req: HttpServletRequest, context: WebContext){
        val id = req.getParameter("id").toLong()
        val user = req.session.getAttribute("user") as User

        val post = checkPost(id, user.id)
        context.setVariable("post", post)
    }

    fun deletePost(req: HttpServletRequest, resp: HttpServletResponse){
        val id = req.getParameter("id").toLong()
        val user = req.session.getAttribute("user") as User
        checkPost(id, user.id)
        postRepository.deleteById(id)
        resp.sendRedirect("${req.contextPath}/mvc/post/list")
    }



    private fun checkPost(id: Long, userId: Long): Post {
        val post = postRepository.findById(id).orElseThrow()
        if (userId != post.user.id) throw ResponseStatusException(
            HttpStatus.UNAUTHORIZED, "권한이 없습니다.")  // 401
        return post
    }

}