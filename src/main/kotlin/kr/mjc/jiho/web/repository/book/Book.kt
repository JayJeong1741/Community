package kr.mjc.jiho.web.repository.book

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Book{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long = 0
    lateinit var title:String
    lateinit var author:String
}