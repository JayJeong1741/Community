package kr.mjc.jiho.web.repository.post

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface PostRepository : JpaRepository<Post, Long> {
    @Query("select p from Post p where p.type = 1 order by p.id desc")
    fun findAllPostByOrderByIdDesc(pageable: PageRequest): Slice<Post>

    @Query("select p from Post p where p.type = 2 order by p.id desc")
    fun findAllClubByOrderByIdDesc(pageable: PageRequest): Slice<Post>


    /** 글수정 */
    @Modifying
    @Transactional
    @Query("update Post set title=:#{#post.title}, content=:#{#post.content} where id=:#{#post.id}")
    fun update(post: Post)

}