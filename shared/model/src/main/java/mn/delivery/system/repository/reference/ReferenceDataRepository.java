package mn.delivery.system.repository.reference;

import mn.delivery.system.model.reference.ReferenceData;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReferenceDataRepository extends MongoRepository<ReferenceData, String> {


    long countByTypeCodeAndAndDeletedFalse(String typeCode);

    boolean existsByOrderAndTypeCodeAndDeletedFalse(Integer order, String typeCode);

    boolean existsByTypeCodeAndDeletedFalse(String typeCode);

    boolean existsByTypeCodeAndIdNotAndDeletedFalse(String typeCode, String id);

    boolean existsByIdNotAndOrderAndTypeCodeAndDeletedFalse(String id, Integer order, String typeCode);

    List<ReferenceData> findAllByTypeCodeAndDeletedFalseOrderByOrder(String typeCode);

    List<ReferenceData> findAllByTypeCodeAndOrderGreaterThanEqualAndDeletedFalseOrderByOrder(String typeCode, Integer order, Sort sort);

    List<ReferenceData> findAllByTypeCodeEqualsAndOrderBetweenAndDeletedFalseOrderByOrder(String typeId, Integer startOrder, Integer endOrder, Sort sort);

    List<ReferenceData> findAllByTypeCodeEqualsAndNameContainingAndDeletedFalse(String typeCode, String name);

    List<ReferenceData> findAllByIdIn(List<String> ids);
}

