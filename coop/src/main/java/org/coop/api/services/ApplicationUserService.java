package org.coop.api.services;

import java.util.ArrayList;

import org.coop.api.model.user.ApplicationUser;
import org.coop.api.model.user.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService implements UserDetailsService {

	@Autowired
	private ApplicationUserRepository applicationUserRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		ApplicationUser user = applicationUserRepository.findByUsername(username);
		return new User(user.getUsername(), user.getPassword(), new ArrayList<>());
	}

}
