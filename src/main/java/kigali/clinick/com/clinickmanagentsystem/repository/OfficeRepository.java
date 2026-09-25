package kigali.clinick.com.clinickmanagentsystem.repository;

import kigali.clinick.com.clinickmanagentsystem.model.Office;
import org.hibernate.internal.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OfficeRepository extends JpaRepository<Office, Long> {

    Office findByOfficeNumber(int officeNumber);

    Optional<Office> findByNameAndOfficeNumber(String name, int officeNumber);

    Boolean existsByOfficeNumber(int officeNumber);

    List<Office> getByOfficeNumber(int officeNumber);
}

