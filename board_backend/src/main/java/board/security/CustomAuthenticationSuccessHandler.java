package board.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import board.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomAuthenticationSuccessHandler { // implements AuthenticationSuccessHandler {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private Environment env;
	
//	@Override
//	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//			Authentication authentication) throws IOException, ServletException {
//		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
//		UserEntity userEntity = userRepository.findByUsername(userDetails.getUsername());
//		
//		// 로그인 성공 후 수행할 작업들을 기술
//		// 예) 데이터베이스에 접속 로그를 기록, 알림 이메일을 발송, ...
//		
//		// 토큰 발생 시간과 만료 시간 설정에 필요한 값
//		Instant now = Instant.now();
//		Long expirationTime = Long.parseLong(env.getProperty("token.expiration-time"));
//		
//		// JWT 토큰을 생성해서 로그로 기록
//		String jwtToken = Jwts.builder()
//				.claim("name", userEntity.getName())
//				.claim("email", userEntity.getEmail())
//				.setSubject(userEntity.getUsername())
//				.setId(String.valueOf(userEntity.getSeq()))
//				.setIssuedAt(Date.from(now))
//				.setExpiration(Date.from(now.plus(expirationTime, ChronoUnit.MILLIS)))
//				.compact();
//		log.debug(jwtToken);		
//		
//		// 세션에 사용자 정보를 저장
//		request.getSession().setAttribute("user", userEntity);
//
//		// 리다이렉트 
//		response.sendRedirect("/home");
//	}
}
