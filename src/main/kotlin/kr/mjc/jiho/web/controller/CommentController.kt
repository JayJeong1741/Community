package kr.mjc.jiho.web.controller

import kr.mjc.jiho.web.repository.comment.Comments
import kr.mjc.jiho.web.repository.comment.CommentsRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

class CommentController(private val commentRepository : CommentsRepository) {

    @GetMapping("/post/detail")
    fun detail(@RequestParam("id") id : Long){

    }
}