package mn.delivery.system.service;

import lombok.RequiredArgsConstructor;
//import mn.astvision.autoland.dao.ArticleDao;

import mn.delivery.system.dao.MultiLanguageDao;
import mn.delivery.system.model.MultiLanguage;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MultiLanguageService {

    private final MultiLanguageDao multiLanguageDao;
//    private final UserService userService;

    public Iterable<MultiLanguage> list(String code, String app, String name, String nomenclatureMN, String nomenclatureEN, Integer order, Pageable pageable) {
        Iterable<MultiLanguage> listData = multiLanguageDao.list(code,app,name,nomenclatureMN, nomenclatureEN, order, pageable);

        for (MultiLanguage multiLanguage : listData) {
            fillRelatedData(multiLanguage);
        }

        return listData;
    }

    private void fillRelatedData(MultiLanguage multiLanguage) {
//        if (referenceData.getCreatedBy() != null) {
//            referenceData.setCreatedUserFullName(userService.getFullNameById(referenceData.getCreatedBy()));
//        }

//        if (!ObjectUtils.isEmpty(referenceData.getTypeCode())) {
//            ReferenceType type = referenceTypeRepository.findByCodeAndDeletedFalse(referenceData.getTypeCode());
//            if (type != null) {
//                referenceData.setTypeName(type.getName());
//            }
//        }
    }
}
