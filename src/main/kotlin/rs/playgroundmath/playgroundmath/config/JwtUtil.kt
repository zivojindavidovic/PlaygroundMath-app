package rs.playgroundmath.playgroundmath.config

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtUtil {
    //private val secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256)
    private val secretKey = "SuperSecretKeyWithAtLeast256Bits1234567890123456";

    fun generateToken(email: String): String
    {
        val claims = mapOf("email" to email)

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(email)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
            .signWith(SignatureAlgorithm.HS256, secretKey.toByteArray())
            .compact()
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean
    {
        val email = extractEmail(token)
        return email == userDetails.username && !isTokenExpired(token)
    }

    fun isTokenExpired(token: String): Boolean
    {
        val expiration = extractAllClaims(token).expiration
        return expiration.before(Date())
    }

    fun extractEmail(token: String): String?
    {
        val extracted = extractAllClaims(token).subject
        return extractAllClaims(token).subject
    }

    private fun extractAllClaims(token: String): Claims
    {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey.toByteArray())
            .build()
            .parseClaimsJws(token)
            .body
    }
}