package kr.mjc.jiho.web.repository.post

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class PostService(private val postRepository: PostRepository) {
    @Transactional(readOnly = true)
    fun getPostWithComments(postId: Long): Post {
        return postRepository.findById(postId)
            .orElseThrow {
                IllegalArgumentException(
                    "Post not found"
                )
            }
    }
}
