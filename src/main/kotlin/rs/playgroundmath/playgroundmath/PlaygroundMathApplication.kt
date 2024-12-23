package rs.playgroundmath.playgroundmath

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication

@SpringBootApplication
@EntityScan(basePackages = arrayOf("rs.playgroundmath.playgroundmath"))
open class PlaygroundMathApplication

fun main(args: Array<String>) {
    runApplication<PlaygroundMathApplication>(*args)
}
