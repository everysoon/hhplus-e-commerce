package kr.hhplus.be.server.domain.user;

import java.util.List;

public interface UserRepository {
	User findById(Long userId);
	User save(User user);
	List<User> findAll();
}
