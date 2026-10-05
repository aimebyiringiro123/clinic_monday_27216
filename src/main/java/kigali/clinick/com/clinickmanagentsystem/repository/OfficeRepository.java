package kigali.clinick.com.clinickmanagentsystem.repository;

import kigali.clinick.com.clinickmanagentsystem.model.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OfficeRepository extends JpaRepository<Office, UUID> {

    Optional<Office> findByOfficeNumber(int officeNumber);

    Optional<Office> findByNameAndOfficeNumber(String name, int officeNumber);

    Boolean existsByOfficeNumber(int officeNumber);

    List<Office> getByOfficeNumber(int officeNumber);

    @Query("SELECT o.name, o.officeNumber, COUNT(a) FROM Appointment a JOIN a.doctor d JOIN d.office o " +
            "GROUP BY o.name, o.officeNumber ORDER BY COUNT(a) DESC")
    List<Object[]> findBusiestOffice();
}
