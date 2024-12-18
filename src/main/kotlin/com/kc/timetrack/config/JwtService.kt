package com.kc.timetrack.config
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.Date
import java.util.UUID
import javax.crypto.SecretKey

@Service
class JwtService {

    private val secretKey: SecretKey

    init {
        val keyString = "kmpclelongueetsecurisee1234567890"
        if (keyString.length < 32) {
            throw IllegalArgumentException("Error secret key")
        }
        secretKey = Keys.hmacShaKeyFor(keyString.toByteArray())
    }

    fun extractUsername(token: String): String? =
        extractClaim(token) { claims -> claims.subject }

    fun extractEmployeeId(token: String): UUID? =
        extractClaim(token) { claims -> claims["employeeId"] as? UUID }

    fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver.invoke(claims)
    }

    fun extractAllClaims(token: String): Claims =
        Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).body

    fun isTokenValid(token: String, userDetails: org.springframework.security.core.userdetails.UserDetails): Boolean {
        val username = extractUsername(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    fun isTokenExpired(token: String): Boolean =
        extractAllClaims(token).expiration.before(Date())

    fun generateToken(userDetails: UserDetails): String {
        val claims = HashMap<String, Any>()
        return createToken(claims, userDetails.username)
    }

    private fun createToken(claims: Map<String, Any>, subject: String): String {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 heures
            .signWith(secretKey)
            .compact()
    }

}