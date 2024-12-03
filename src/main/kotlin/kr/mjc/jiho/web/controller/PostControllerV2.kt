package kr.mjc.jiho.web.controller

import jakarta.servlet.http.HttpSession
import kr.mjc.jiho.web.repository.comment.Comments
import kr.mjc.jiho.web.repository.comment.CommentService
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
class PostControllerV2(
    private val postRepository: PostRepository,
    private val commentService: CommentService
) {
    companion object {
        private const val PAGE_SIZE = 10
    }

    @GetMapping("/club/list")
    fun clubList(page: Int = 0, session: HttpSession, model: Model) {
        session.setAttribute("page", page)
        val posts:Slice<Post> = postRepository.findAllClubByOrderByIdDesc(PageRequest.of(page, PAGE_SIZE))
        model.addAttribute("list", posts)
    }

    @GetMapping("/post/list")
    fun list(page:Int = 0, session: HttpSession, model: Model){
        session.setAttribute("page", page)//원래 목록으로 돌아가기 위한 설정
        val posts:Slice<Post> =
            postRepository.findAllPostByOrderByIdDesc(PageRequest.of(page, PAGE_SIZE))
        model.addAttribute("list", posts)
    }

    // /post/create 접속 시 interceptor 실행 후 create메소드 실행
    /** 글쓰기 화면 */
    @GetMapping("/post/create")
    fun postCreate() {
    }

    @PostMapping("/post/create")
    fun postCreate(post: Post, @SessionAttribute user:User, session: HttpSession): String{
        post.apply {
            this.user = user
            pubDate = LocalDateTime.now()
            lastModified =LocalDateTime.now()
            type = 1;
        }
        postRepository.save(post)
        return "redirect:/post/list"
    }

    @GetMapping("/club/create")
    fun clubCreate() {
    }

    @PostMapping("/club/create")
    fun clubCreate(post: Post, @SessionAttribute user:User, session: HttpSession): String{
        post.apply {
            this.user = user
            pubDate = LocalDateTime.now()
            lastModified =LocalDateTime.now()
            type = 2;
        }
        postRepository.save(post)
        return "redirect:/club/list"
    }

    @GetMapping("/post/detail")
    fun postDetail(id: Long, model: Model) {
        val post: Post = postRepository.findById(id).orElseThrow()
        val comments = commentService.getCommentsByPostId(id)
        model.addAttribute("post", post)
        model.addAttribute("comments", comments)
    }
    @GetMapping("/club/detail")
    fun clubDetail(id: Long, model: Model) {
        val post: Post = postRepository.findById(id).orElseThrow()
        val comments = commentService.getCommentsByPostId(id)
        model.addAttribute("post", post)
        model.addAttribute("comments", comments)
    }

    @PostMapping("/post/comment")
    fun addComment(
        @RequestParam postId: Long,
        @RequestParam content: String,
        @SessionAttribute user: User
    ): String {
        val post = postRepository.findById(postId).orElseThrow()
        val comment = Comments().apply {
            this.user = user
            this.post = post
            this.content = content
        }
        commentService.addComment(comment)
        return "redirect:/post/detail?id=$postId"
    }

    /** 글수정 화면 */
    @GetMapping("/post/update")
    fun update(@SessionAttribute user: User, id: Long , model: Model) {
        val post = checkPost(id, user.id)
        model.addAttribute("post", post)
    }
    /*업데이트 구현*/
    @PostMapping("/post/update")
    fun postUpdate(@SessionAttribute user:User, post:Post):String {

        checkPost(post.id, user.id)
        postRepository.update(post)
        return "redirect:/post/detail?id=${post.id}"
    }
    @GetMapping("/club/update")
    fun clubUpdate(@SessionAttribute user: User, id: Long , model: Model) {
        val post = checkPost(id, user.id)
        model.addAttribute("post", post)
    }
    @PostMapping("/club/update")
    fun clubUpdate(@SessionAttribute user:User, post:Post):String {

        checkPost(post.id, user.id)
        postRepository.update(post)
        return "redirect:/club/detail?id=${post.id}"
    }

    @PostMapping("post/delete")
    fun postDelete(id:Long, session: HttpSession,@SessionAttribute user: User ,
               @SessionAttribute page : Int):String{
        checkPost(id, user.id)
        postRepository.deleteById(id)
        return "redirect:/post/list?page=$page"
    }
    @PostMapping("club/delete")
    fun clubDelete(id:Long, session: HttpSession,@SessionAttribute user: User ,
               @SessionAttribute page : Int):String{
        checkPost(id, user.id)
        postRepository.deleteById(id)
        return "redirect:/club/list?page=$page"
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