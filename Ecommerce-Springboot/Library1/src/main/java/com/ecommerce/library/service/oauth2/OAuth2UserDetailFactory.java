package com.ecommerce.library.service.oauth2;

import java.util.Map;

import com.ecommerce.library.model.Provider;

public class OAuth2UserDetailFactory {
	public static OAuth2UserDetails getOAuth2UserDetails(String registrationId, Map<String, Object> attributes) {
		if(registrationId.equals(Provider.google.name())) {
			return new OAuth2GoogleUser(attributes);
		}else if(registrationId.equals(Provider.facebook.name())) {
			return new OAuth2FacebookUser(attributes);
		}else if(registrationId.equals(Provider.github.name())) {
			return new OAuth2GithubUser(attributes);
		}else {
			return null;
		}
	}
}
