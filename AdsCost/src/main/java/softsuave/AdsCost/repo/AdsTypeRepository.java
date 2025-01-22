package softsuave.AdsCost.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import softsuave.AdsCost.entity.AdsCostTabel;
import softsuave.AdsCost.enums.AdsType;


public interface AdsTypeRepository extends JpaRepository<AdsCostTabel, Integer> {

    AdsCostTabel findByType(AdsType Type);
}
