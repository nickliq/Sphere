package ru.nickliq.sphere.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nickliq.sphere.domain.SituationStored;

import java.util.List;

@Repository
public interface SituationStoredRepository extends JpaRepository<SituationStored, Integer> {
}
