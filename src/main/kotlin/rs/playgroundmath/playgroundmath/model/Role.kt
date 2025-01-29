package rs.playgroundmath.playgroundmath.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import rs.playgroundmath.playgroundmath.enums.RoleType

@Entity
@Table(name = "role")
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false)
    val roleId: Long = 0,

    @Column(name = "role_type", nullable = false, columnDefinition = "ENUM('PARENT', 'TEACHER', 'ADMIN')")
    @Enumerated(EnumType.STRING)
    val roleType: RoleType = RoleType.PARENT
)