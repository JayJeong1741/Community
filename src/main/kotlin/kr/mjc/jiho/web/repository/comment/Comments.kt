package kr.mjc.jiho.web.repository.comment

import jakarta.persistence.*
import kr.mjc.jiho.web.formatted
import kr.mjc.jiho.web.repository.post.Post
import kr.mjc.jiho.web.repository.user.User
import java.time.LocalDateTime

@Entity
class Comments{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long = 0
    @ManyToOne
    @JoinColumn(name = "user_id") lateinit var user: User
    @ManyToOne @JoinColumn(name = "post_id") lateinit var post: Post
    lateinit var content: String
    lateinit var createdAt: LocalDateTime
    lateinit var updatedAt: LocalDateTime

    override fun toString(): String {
        return "Post(id=$id, content='$content', pubDate=${createdAt.formatted}, updateDate=${updatedAt.formatted}, user=$user, post=$post)"
    }
}