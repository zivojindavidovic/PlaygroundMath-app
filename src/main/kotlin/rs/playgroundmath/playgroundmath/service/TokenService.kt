package rs.playgroundmath.playgroundmath.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import rs.playgroundmath.playgroundmath.config.JwtProperties
import java.util.Date

@Service
class TokenService(
    private val jwtProperties: JwtProperties,
) {
    private val secretKey = Keys.hmacShaKeyFor(
        jwtProperties.key.toByteArray()
    )

    fun generateToken(
        userDetails: UserDetails,
        expirationDate: Date,
        additionalClaims: Map<String, Any> = emptyMap()
    ): String =
        Jwts.builder()
            .claims()
            .subject(userDetails.username)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(expirationDate)
            .add(additionalClaims)
            .and()
            .signWith(secretKey)
            .compact()

    fun getExtractedEmail(jwtToken: String): String? =
        getAllClaimsFromJwtToken(jwtToken)
            .subject

    fun isTokenExpired(jwtToken: String): Boolean =
        getAllClaimsFromJwtToken(jwtToken)
            .expiration
            .before(Date(System.currentTimeMillis()))

    fun isTokenValid(jwtToken: String, userDetails: UserDetails): Boolean {
        val email = getExtractedEmail(jwtToken)
        return email == userDetails.username && !isTokenExpired(jwtToken)
    }

    fun getAllClaimsFromJwtToken(jwtToken: String): Claims {
        val parser = Jwts.parser()
            .verifyWith(secretKey)
            .build()

        return parser.parseSignedClaims(jwtToken)
            .payload
    }
}