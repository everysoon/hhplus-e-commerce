package kr.hhplus.be.server.config.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public abstract class InMemoryRepository<ID, T> {

	protected final Map<ID, T> store = new ConcurrentHashMap<>();

	public Optional<T> findById(ID id) {
		return Optional.ofNullable(store.get(id));
	}

	public List<T> findAll() {
		return new ArrayList<>(store.values());
	}

	public T save(ID id, T entity) {
		store.put(id, entity);
		return entity;
	}

	public void deleteById(ID id) {
		store.remove(id);
	}

	public void clear() {
		store.clear();
	}
}

