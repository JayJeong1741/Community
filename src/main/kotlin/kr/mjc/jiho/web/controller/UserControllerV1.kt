package kr.mjc.jiho.web.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.mjc.jiho.web.repository.user.User
import kr.mjc.jiho.web.repository.user.UserRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import java.time.LocalDateTime

//@Controller
class UserControllerV1(val userRepository: UserRepository,
                       val passwordEncoder: PasswordEncoder) {

    companion object {
        private const val PAGE_SIZE: Int = 20
        private val sort = Sort.by("id").descending()
        private const val LANDING_PAGE = "/user/list"
    }

    /** 회원 목록 */
    @GetMapping("/user/list")
    fun userList(req: HttpServletRequest, model: Model) {
        val page = req.getParameter("page")?.toInt() ?: 0
        req.session.setAttribute("page", page)//현재 페이지를 세션에 저장 -> detail에 접속 후 돌아왔을 때 페이지를 기억하기 위해
        val users = userRepository.findAll(PageRequest.of(page, PAGE_SIZE, sort))
        model.addAttribute("users", users)
    }

    /** 회원 정보 */
    @GetMapping("/user/detail")
    fun userDetail(req: HttpServletRequest, model: Model) {
        val id = req.getParameter("id").toLong()
        val user = userRepository.findById(id).orElseThrow()
        model.addAttribute("user", user)
    }

    /** 일반 화면 */
    @GetMapping("/user/signup", "/user/login", "/user/profile")
    fun pass() {
        // Do nothing
    }

    /** 회원가입 */
    @PostMapping("/user/signup")
    fun signup(req: HttpServletRequest, resp: HttpServletResponse) {
        val username = req.getParameter("username")
        val exists = userRepository.existsByUsername(username)
        if (!exists) {   // 이메일이 없음. 등록 진행
            val user = User().apply {
                this.username = username
                password = passwordEncoder.encode(req.getParameter("password"))
                firstName = req.getParameter("firstName")
                dateJoined = LocalDateTime.now()
                lastLogin = LocalDateTime.now()
            }
            userRepository.save(user) // 등록 성공
            req.session.setAttribute("user", user)  // 로그인
            resp.sendRedirect("${req.contextPath}${LANDING_PAGE}")
        } else {  // 이메일 존재. 등록 실패
            resp.sendRedirect("${req.contextPath}/user/signup?error")
        }
    }

    /** 로그인 */
    @PostMapping("/user/login")
    fun login(req: HttpServletRequest, resp: HttpServletResponse) {
        val username = req.getParameter("username")
        val password = req.getParameter("password")

        val user = userRepository.findByUsername(username)
        if (user != null && passwordEncoder.matches(password, user.password)) {
            // 비밀번호 매치
            req.session.setAttribute("user", user)
            user.lastLogin = LocalDateTime.now()
            userRepository.updateLastLogin(user.id, user.lastLogin)
            resp.sendRedirect("${req.contextPath}${LANDING_PAGE}")
        } else {  // 사용자가 없거나 비밀번호가 매치하지 않을 경우
            resp.sendRedirect("${req.contextPath}/user/login?error")
        }
    }

    /** 로그아웃 */
    @PostMapping("/user/logout")
    fun logout(req: HttpServletRequest, resp: HttpServletResponse) {
        req.session.invalidate()
        resp.sendRedirect("${req.contextPath}${LANDING_PAGE}")
    }
    /** 해지 */
    @PostMapping("/user/delete")
    fun delete(req: HttpServletRequest, resp: HttpServletResponse) {
        val user = req.session.getAttribute("user") as User
        try {
            userRepository.deleteById(user.id)
        } catch (e: DataIntegrityViolationException) {
            throw DataIntegrityViolationException(
                "등록한 글들이 있어서 해지할 수 없습니다.\n글들을 먼저 삭제하세요.")
        }
        logout(req, resp)
    }
}