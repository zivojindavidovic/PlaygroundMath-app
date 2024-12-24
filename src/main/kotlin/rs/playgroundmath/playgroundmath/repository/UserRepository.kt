package rs.playgroundmath.playgroundmath.repository

import org.springframework.data.jpa.repository.JpaRepository
import rs.playgroundmath.playgroundmath.model.User

interface UserRepository: JpaRepository<User, Long> {
}