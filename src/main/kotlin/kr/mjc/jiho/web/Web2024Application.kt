package kr.mjc.jiho.web

import kr.mjc.jiho.web.interceptor.AuthInterceptor
import kr.mjc.jiho.web.interceptor.CsrfInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@SpringBootApplication
@ServletComponentScan
class Web2024Application : WebMvcConfigurer {

    @Autowired lateinit var authInterceptor: AuthInterceptor
    @Autowired lateinit var csrfInterceptor: CsrfInterceptor

    override fun addInterceptors(registry: InterceptorRegistry) {//spring security쓰면 자동 구현
        registry.addInterceptor(authInterceptor)
            .addPathPatterns("/post/list", "/post/create", "/post/update", "/post/delete",
                "/user/profile", "/user/logout", "/user/delete",
                "/club/list", "/club/create", "/club/update", "/club/delete")

        registry.addInterceptor(csrfInterceptor)
            .addPathPatterns("/user/login", "/user/logout", "/user/signup",
                "/user/profile", "/post/create", "/post/update",
                "/post/detail","/post/delete","/post/myPostList", "/post/deletePostList",
                "/club/create", "/club/update",
                "/club/detail","/club/delete")
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}

fun main(args: Array<String>) {
    val applicationContext =  runApplication<Web2024Application>(*args)
    applicationContext.beanDefinitionNames.forEachIndexed{index, name ->
        println("$index : $name")
    }
}
