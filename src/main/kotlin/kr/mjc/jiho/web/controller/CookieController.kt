package kr.mjc.jiho.web.controller

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class CookieController {

    @GetMapping("/cookie/set")
    fun set(resp: HttpServletResponse): String {
        val cookie1 = Cookie("myName", "Jacob").apply {
            path = "/"  // 쿠키 적용 경로
            maxAge = 3600 // 쿠키 유지 시간(초)
        }
        val cookie2 = Cookie("yourName", "Rachel").apply {
            path = "/"  // 쿠키 적용 경로
            maxAge = 3600 // 쿠키 유지 시간(초)
        }
        resp.addCookie(cookie1)
        resp.addCookie(cookie2)
        return "cookie/set"
    }
    @GetMapping("/cookie/get")
    fun get(req: HttpServletRequest, model: Model) {
        val cookies: Array<Cookie>? = req.cookies
        // 쿠키 배열을 쿠키 이름과 쿠키 값의 맵으로 바꾼다.
        val cookieMap = cookies?.associate { cookie -> cookie.name to cookie.value }
        model.addAttribute("cookieMap", cookieMap)
    }

    @GetMapping("/cookie/delete")
    fun delete(req: HttpServletRequest, resp: HttpServletResponse,
               model: Model): String {
        req.cookies?.forEach { cookie ->
            cookie.path = "/"
            cookie.maxAge = 0 // 쿠키 유지 시간을 0으로 설정해서 삭제한다.
            resp.addCookie(cookie)
        }
        return "redirect:/cookie/get"
    }

}