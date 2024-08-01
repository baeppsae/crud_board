package board.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import board.security.CustomAuthenticationSuccessHandler;
import board.security.JwtRequestFilter;

@Configuration
// @EnableWebSecurity
public class SecurityConfiguration {
	@Autowired
	private CustomAuthenticationSuccessHandler successHandler;
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	
//	@Bean
//	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		http
//			.authorizeHttpRequests((auth) -> auth
//				.requestMatchers("/", "/login", "/home", "/join", "/joinProc").permitAll()	
//				.requestMatchers("/admin").hasRole("ADMIN")
//				.requestMatchers("/board/**", "/api/**").hasAnyRole("ADMIN", "USER")
//				.anyRequest().authenticated()
//			);
//		http
//			.formLogin((auth) -> auth 
//				.loginPage("/login")
//				.loginProcessingUrl("/loginProc")
//				.permitAll()
//				// .defaultSuccessUrl("/board/openBoardList.do")
//				.successHandler(successHandler)
//			);
//		
//		http
//		 	.csrf((auth) -> auth.disable());
//		
//		http
//			.sessionManagement((auth) -> auth
//				.sessionFixation((sessionFixation) -> sessionFixation
//					.newSession()
//					.maximumSessions(1)
//					.maxSessionsPreventsLogin(true))
//			);
//		
//		http
//			.logout((auth) -> auth
//				.logoutUrl("/logout")
//				.logoutSuccessUrl("/")
//			);
//		
//		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//		
//		return http.build();
//	}
//	
//	@Bean
//	BCryptPasswordEncoder bCryptPasswordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
}
