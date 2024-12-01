package kr.mjc.jiho.web.repository.comment

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentsRepository : JpaRepository<Comments, Long> {
    fun findByPostIdOrderByCreatedAtDesc(postId: Long): List<Comments>
}