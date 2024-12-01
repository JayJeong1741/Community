package kr.mjc.jiho.web.repository.user

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.io.Serializable
import java.time.LocalDateTime
import kr.mjc.jiho.web.formatted

@Entity
class User : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) var id : Long = 0
    lateinit var username: String
    lateinit var password: String
    lateinit var firstName: String
    lateinit var dateJoined: LocalDateTime
    lateinit var lastLogin: LocalDateTime

    val dateJoinedFormatted get() = dateJoined.formatted

    val lastLoginFormatted get() = lastLogin.formatted

    override fun toString(): String =
        "User(id=$id, username='$username', firstName='$firstName', dateJoined=${dateJoined.formatted}, lastLogin=${lastLogin.formatted})"
}