package kigali.clinick.com.clinickmanagentsystem.repository;

import kigali.clinick.com.clinickmanagentsystem.model.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SpecializationRepository extends JpaRepository<Specialization, Long> {

    // B3
    @Query("SELECT s FROM Specialization s WHERE s.doctors IS EMPTY")
    List<Specialization> findUnused();

}
