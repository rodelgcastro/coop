package org.coop.api.controllers;

import java.util.List;

import org.coop.api.model.user.ApplicationUser;
import org.coop.api.model.user.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coop/api/user")
public class ApplicationUserController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private ApplicationUserRepository applicationUserRepository;

	@GetMapping("/list")
	@ResponseStatus(HttpStatus.OK)
	public List<ApplicationUser> getApplicationUsers() {
		List<ApplicationUser> users = applicationUserRepository.findAll();
		return users;
	}

	@GetMapping("/{username}")
	@ResponseStatus(HttpStatus.OK)
	public ApplicationUser getApplicationUserByUsername(@PathVariable String username) {
		ApplicationUser user = applicationUserRepository.findByUsername(username);
		user.setPassword(null);
		return user;
	}

	@PostMapping("/add/one")
	@ResponseStatus(HttpStatus.CREATED)
	public void addApplicationUser(@RequestBody ApplicationUser user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		applicationUserRepository.save(user);
	}

}
