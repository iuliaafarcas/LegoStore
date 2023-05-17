package org.example.repository;

import org.example.model.LegoStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LegoStoreRepository extends JpaRepository<LegoStore, Long> {
}
