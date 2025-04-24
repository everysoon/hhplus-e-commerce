package kr.hhplus.be.server.domain.user.repository;

import kr.hhplus.be.server.domain.user.User;

import java.util.List;

public interface UserRepository {
	User findById(Long userId);
	User save(User user);
	List<User> findAll();
}
