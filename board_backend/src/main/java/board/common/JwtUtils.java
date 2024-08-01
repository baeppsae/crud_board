package board.common;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import board.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtils {
	private Key hmacKey;
	private Long expirationTime;
		
	public JwtUtils(Environment env) {
		this.hmacKey = hmacKey = new SecretKeySpec(
			Base64.getDecoder().decode(env.getProperty("token.secret")), SignatureAlgorithm.HS256.getJcaName()
		);
		this.expirationTime = Long.parseLong(env.getProperty("token.expiration-time"));
	}
	
	public String generateToken(UserEntity userEntity) {
		Instant now = Instant.now();
		String jwtToken = Jwts.builder()
				.claim("name", userEntity.getName())
				.claim("email", userEntity.getEmail())
				// .setSubject(userEntity.getUsername())
				// .setId(String.valueOf(userEntity.getSeq()))
				// .setIssuedAt(Date.from(now))
				// .setExpiration(Date.from(now.plus(expirationTime, ChronoUnit.MILLIS)))
				.claim("sub", userEntity.getUsername())
				.claim("jti", String.valueOf(userEntity.getSeq()))
				.claim("iat", Date.from(now))
				.claim("exp", Date.from(now.plus(expirationTime, ChronoUnit.MILLIS)))
				.signWith(hmacKey)
				.compact();
		log.debug(jwtToken);	
		return jwtToken;
	}
	
	private Claims getAllClaimsFromToken(String token) {
		Jws<Claims> jwt = Jwts.parser().setSigningKey(hmacKey).build().parseClaimsJws(token);
		return jwt.getBody();
	}
	
	public String getSubjectFromToken(String token) {
		final Claims claims = getAllClaimsFromToken(token);
		return claims.getSubject();
	}
		
	private Date getExiprationDateFromToken(String token) {
		final Claims claims = getAllClaimsFromToken(token);
		return claims.getExpiration();
	}
	
	private boolean isTokenExpired(String token) {
		Date expiration = getExiprationDateFromToken(token);
		return expiration.before(new Date());
	}
	
	public boolean validateToken(String token, UserEntity userEntity) {
		// 토큰 유효기간 체크
		if (isTokenExpired(token)) {
			return false;
		}
		
		// 토큰 내용을 검증 
		String subject = getSubjectFromToken(token);
		String username = userEntity.getUsername();
		
		return subject != null && username != null && subject.equals(username);
	}
}
