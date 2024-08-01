package board.dto;

import board.entity.UserEntity;

public class CustomUserDetail { // implements UserDetails {
	private UserEntity userEntity;
	
	public CustomUserDetail(UserEntity userEntity) {
		this.userEntity = userEntity;
	}

//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		Collection<GrantedAuthority> collection = new ArrayList<>();
//		collection.add(new GrantedAuthority() {
//			@Override
//			public String getAuthority() {
//				return userEntity.getRole();
//			}
//		});		
//		return collection;
//	}
//	@Override
//	public String getPassword() {
//		return userEntity.getPassword();
//	}
//
//	@Override
//	public String getUsername() {
//		return userEntity.getUsername();
//	}

}
