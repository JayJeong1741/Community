package kr.mjc.jiho.web.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.SessionAttribute
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@Controller
class PostControllerV2(private val postRepository : PostRepository) {
    companion object {
        private const val PAGE_SIZE = 10
    }

    @GetMapping("/post/list")
    fun list(page:Int = 0, session: HttpSession, model: Model){
        session.setAttribute("page", page)//원래 목록으로 돌아가기 위한 설정
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
    fun create(post: Post, @SessionAttribute user:User, session: HttpSession): String{
        post.apply {
            this.user = user
            pubDate = LocalDateTime.now()
            lastModified =LocalDateTime.now()
        }
        postRepository.save(post)
        return "redirect:/post/list"
    }

    @GetMapping("/post/detail")
    fun detail(id: Long, model: Model){
       val post : Post = postRepository.findById(id).orElseThrow()
        model.addAttribute("post", post)
    }

    /** 글수정 화면 */
    @GetMapping("/post/update")
    fun update(@SessionAttribute user: User, id: Long , model: Model) {
        val post = checkPost(id, user.id)
        model.addAttribute("post", post)
    }
    /*업데이트 구현*/
    @PostMapping("/post/update")
    fun update(@SessionAttribute user:User, post:Post):String {

        checkPost(post.id, user.id)
        postRepository.update(post)
        return "redirect:/post/detail?id=${post.id}"
    }

    @PostMapping("post/delete")
    fun delete(id:Long, session: HttpSession,@SessionAttribute user: User ,
               @SessionAttribute page : Int):String{
        checkPost(id, user.id)
        postRepository.deleteById(id)
        return "redirect:/post/list?page=$page"
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