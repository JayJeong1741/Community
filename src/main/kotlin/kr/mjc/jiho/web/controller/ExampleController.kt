package kr.mjc.jiho.web.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpSession
import kr.mjc.jiho.web.repository.user.User
import kr.mjc.jiho.web.repository.user.UserRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.SessionAttribute
import org.springframework.web.servlet.ModelAndView

@Controller
class ExampleController(val userRepository: UserRepository) {

    @GetMapping("/handler-methods/")
    fun index(): String {
        return "handler-methods/index" // /templates/handler-methods/index.html
    }

    /** request.getParameter() */
    @PostMapping("/handler-methods/request")
    fun request(request: HttpServletRequest, model: Model): String {
        val user = User().apply {
            this.username = request.getParameter("username")
            this.password = request.getParameter("password")
            this.firstName = request.getParameter("firstName")
        }
        model.addAttribute("user", user)
        return "handler-methods/user" // /templates/handler-methods/user.html
    }

    /** @RequestParam */
    @PostMapping("/handler-methods/requestParam")
    fun requestParam(
        username: String, password: String, firstName: String, model: Model
    ): String {
        val user = User().apply {
            this.username = username
            this.password = password
            this.firstName = firstName
        }
        model.addAttribute("user", user)
        return "handler-methods/user"
    }

    @PostMapping("/handler-methods/modelAndView")
    fun modelAndView(
        username: String, password: String, firstName: String
    ): ModelAndView {
        val user = User().apply {
            this.username = username
            this.password = password
            this.firstName = firstName
        }
        return ModelAndView("handler-methods/user").addObject("user", user)
    }

    /** @ModelAttribute */
    @PostMapping("/handler-methods/modelAttribute")
    fun modelAttribute(user: User): String {
        return "handler-methods/user"
    }

    /** @SessionAttribute */
    @GetMapping("/handler-methods/profile")
    fun profile(@SessionAttribute("user") user: User?) {
        println("@SessionAttribute: $user")
        // /templates/handler-methods/profile.html
    }

    /** HttpSession */
    @PostMapping("/handler-methods/login")
    fun login(session: HttpSession, username: String): String {
        val user = userRepository.findByUsername(username)
        session.setAttribute("user", user)
        return "redirect:/handler-methods/profile"
    }

    /** session.invalidate() */
    @PostMapping("/handler-methods/logout")
    fun logout(session: HttpSession): String {
        session.invalidate()
        return "redirect:/handler-methods/"
    }
}