package kr.mjc.jiho.web.repository.post

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface PostRepository : JpaRepository<Post, Long> {
    fun findAllByOrderByIdDesc(pageable: PageRequest): Slice<Post>

    /** 글수정 */
    @Modifying
    @Transactional
    @Query("update Post set title=:#{#post.title}, content=:#{#post.content} where id=:#{#post.id}")
    fun update(post: Post)

}