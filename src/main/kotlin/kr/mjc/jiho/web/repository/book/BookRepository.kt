package kr.mjc.jiho.web.repository.book

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository

interface BookRepository : JpaRepository<Book, Long> {
    fun findAllByOrderByIdDesc(pageable: PageRequest): Slice<Book>
}