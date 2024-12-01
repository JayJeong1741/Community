package kr.mjc.jiho.web.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.mjc.jiho.web.repository.post.Post
import kr.mjc.jiho.web.repository.post.PostRepository
import kr.mjc.jiho.web.repository.user.User
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

//@Controller
class PostControllerV1(private val postRepository : PostRepository) {
    companion object {
        private const val PAGE_SIZE = 20
    }

    @GetMapping("/post/list")
    fun list(req:HttpServletRequest, model: Model){
        val page = req.getParameter("page")?.toInt() ?: 0
        req.session.setAttribute("page", page)//원래 목록으로 돌아가기 위한 설정

        val posts:Slice<Post> =
            postRepository.findAllByOrderByIdDesc(PageRequest.of(page, PAGE_SIZE))
        model.addAttribute("list", posts)
    }

    // /post/create 접속 시 interceptor 실행 후 create메소드 실행
    /** 글쓰기 화면 */
    @GetMapping("/post/create")
    fun create() {
    }

    @PostMapping("/post/create")
    fun create(req: HttpServletRequest, resp: HttpServletResponse){
        val user = req.session.getAttribute("user") as User
        val post = Post().apply {
            this.user = user
            title = req.getParameter("title")
            content = req.getParameter("content")
            pubDate = LocalDateTime.now()
            lastModified =LocalDateTime.now()
        }
        postRepository.save(post)
        resp.sendRedirect("${req.contextPath}/post/list")
    }

    @GetMapping("/post/detail")
    fun detail(req: HttpServletRequest, model: Model){
        val id = req.getParameter("id").toLong()
        val post: Post = postRepository.findById(id).orElseThrow()
        model.addAttribute("post", post)
    }

    /** 글수정 화면 */
    @GetMapping("/post/update")
    fun update(req: HttpServletRequest, model: Model) {
        val id = req.getParameter("id").toLong()
        val user = req.session.getAttribute("user") as User

        val post = checkPost(id, user.id)
        model.addAttribute("post", post)
    }
    /*업데이트 구현*/
    @PostMapping("/post/update")
    fun update(req: HttpServletRequest, resp: HttpServletResponse) {
        val id = req.getParameter("id").toLong()
        val user = req.session.getAttribute("user") as User

        val post = checkPost(id, user.id)
        post.apply {
            post.title = req.getParameter("title")
            post.content = req.getParameter("content")
            post.lastModified = LocalDateTime.now()
        }
        postRepository.save(post)
        resp.sendRedirect("${req.contextPath}/post/detail?id=${post.id}")
    }

    @PostMapping("post/delete")
    fun delete(req: HttpServletRequest, resp: HttpServletResponse, model: Model){
        val id = req.getParameter("id").toLong()
        val session = req.session
        val user = req.session.getAttribute("user") as User

        checkPost(id, user.id)
        postRepository.deleteById(id)
        resp.sendRedirect(
            "${req.contextPath}/post/list?page=${session.getAttribute("page")}")
    }
    /** 게시글의 권한 체크
     * @throws ResponseStatusException 권한이 없을 경우
     */
    private fun checkPost(id: Long, userId: Long): Post {
        val post = postRepository.findById(id).orElseThrow()
        if (userId != post.user.id) throw ResponseStatusException(
            HttpStatus.UNAUTHORIZED, "권한이 없습니다.")  // 401
        return post
    }
}