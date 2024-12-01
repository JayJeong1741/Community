package kr.mjc.jiho.web.repository.comment

import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CommentService(private val commentRepository: CommentsRepository) {
    fun addComment(comment: Comments): Comments {
        comment.createdAt = LocalDateTime.now()
        comment.updatedAt = LocalDateTime.now()
        return commentRepository.save(comment)
    }

    fun getCommentsByPostId(postId: Long): List<Comments> {
        return commentRepository.findByPostIdOrderByCreatedAtDesc(postId)
    }
}