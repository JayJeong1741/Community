package kr.mjc.jiho.web.repository.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
    fun existsByUsername(username: String): Boolean

    @Modifying
    @Transactional
    @Query("update User set lastLogin=:lastLogin where id=:id")
    fun updateLastLogin(id: Long, lastLogin: LocalDateTime)
}