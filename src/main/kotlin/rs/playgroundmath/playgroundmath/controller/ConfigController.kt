package rs.playgroundmath.playgroundmath.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/config")
class ConfigController {

    @GetMapping
    fun getAllowedOperations(): Map<String, Int> =
        mapOf(
            "+" to 0,
            "-" to 70,
            "*" to 200,
            "/" to 500
        )
}