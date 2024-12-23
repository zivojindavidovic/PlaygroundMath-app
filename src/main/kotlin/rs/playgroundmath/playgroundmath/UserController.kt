package rs.playgroundmath.playgroundmath

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {

    @GetMapping("/get")
    fun getAllUsers(): String = "Testing"
}
