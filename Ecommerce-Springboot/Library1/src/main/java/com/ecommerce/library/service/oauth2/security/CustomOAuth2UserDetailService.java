package com.ecommerce.library.service.oauth2.security;

import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.ecommerce.library.model.Customer;
import com.ecommerce.library.repository.CustomerRepository;
import com.ecommerce.library.repository.RoleRepository;
import com.ecommerce.library.service.oauth2.OAuth2UserDetailFactory;
import com.ecommerce.library.service.oauth2.OAuth2UserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserDetailService extends DefaultOAuth2UserService{
	
	private final CustomerRepository customerRepository;
	
	private final RoleRepository roleRepository;
	
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		
		try {
			return checkExisted(userRequest, oAuth2User);
		} catch (AuthenticationException e) {
			throw e;
		}catch(Exception ex) {
			throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
		}
	}
	
	
	private OAuth2User checkExisted(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
		OAuth2UserDetails oAuth2UserDetails = OAuth2UserDetailFactory.getOAuth2UserDetails(oAuth2UserRequest.getClientRegistration().getRegistrationId(), 
				oAuth2User.getAttributes());
		if(ObjectUtils.isEmpty(oAuth2UserDetails)) {
			throw new UsernameNotFoundException("Can't not found oauth2 from properties");
		}
		
		Optional<Customer> user = customerRepository.findByUsernameAndProviderId(
				oAuth2UserDetails.getEmail(), oAuth2UserRequest.getClientRegistration().getRegistrationId());
		Customer userDetail;
			if(user.isPresent()) {
				userDetail = user.get();
				if(!userDetail.getProviderId().equals(oAuth2UserRequest.getClientRegistration().getRegistrationId())){
					throw new OAuth2AuthenticationException("Error");
				}
				userDetail =  updateOAuth2UserDetail(userDetail, oAuth2UserDetails);
			}else {
				userDetail = registerOAuth2UserDetail(oAuth2UserRequest, oAuth2UserDetails);
			}
			return new OAuth2UserDetailCustom(
					 userDetail.getId()
					, userDetail.getUsername()
					, userDetail.getPassword()
					,userDetail.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList()));
	}


	public Customer  registerOAuth2UserDetail(OAuth2UserRequest oAuth2UserRequest, OAuth2UserDetails oAuth2UserDetails) {
		Customer user = new Customer();
		user.setUsername(oAuth2UserDetails.getEmail());
		user.setProviderId(oAuth2UserRequest.getClientRegistration().getRegistrationId());
		user.setRoles(new HashSet<>());
		user.getRoles().add(roleRepository.findByName("CUSTOMER"));
		
		return customerRepository.save(user);
	}


	public Customer updateOAuth2UserDetail(Customer user,OAuth2UserDetails oAuth2UserDetails ) {
		user.setUsername(oAuth2UserDetails.getEmail());
		return customerRepository.save(user);
	}
}
