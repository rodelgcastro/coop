package org.coop.api.model.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationUserRepository extends MongoRepository<ApplicationUser, String> {

	@Query("{ 'username' : ?0 }")
	public ApplicationUser findByUsername(String username);

}
