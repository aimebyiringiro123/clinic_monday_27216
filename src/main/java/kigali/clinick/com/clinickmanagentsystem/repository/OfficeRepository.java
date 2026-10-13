package kigali.clinick.com.clinickmanagentsystem.repository;

import kigali.clinick.com.clinickmanagentsystem.model.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface OfficeRepository extends JpaRepository<Office, Long> {
    List<Office> findByName(String name);

    Optional<Office> findByOfficeNumber(String officeNumber);

    @Query("SELECT o FROM Office o " +
            "WHERE NOT EXISTS (SELECT d FROM Doctor d WHERE d.office = o) " +
            "ORDER BY o.officeNumber")
    List<Office> findFreeOffices();
}
