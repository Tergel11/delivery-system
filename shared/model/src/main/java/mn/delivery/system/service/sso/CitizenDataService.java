package mn.delivery.system.service.sso;

import java.time.LocalDateTime;

import mn.delivery.system.dto.sso.CitizenIDCardInfo;
import mn.delivery.system.model.auth.User;
import mn.delivery.system.model.auth.UserCitizenData;
import mn.delivery.system.model.notification.Notification;
import mn.delivery.system.model.notification.enums.NotificationRelatedDataType;
import mn.delivery.system.model.notification.enums.NotificationStatus;
import mn.delivery.system.util.DateUtil;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.repository.auth.UserCitizenDataRepository;
import mn.delivery.system.repository.auth.UserRepository;
import mn.delivery.system.repository.notification.NotificationRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CitizenDataService {

    private final UserRepository userRepository;
    private final UserCitizenDataRepository userCitizenDataRepository;
    private final NotificationRepository notificationRepository;

    public void save(String userId, CitizenIDCardInfo citizenIDCardInfo) {
        UserCitizenData userCitizenData = userCitizenDataRepository.findByUserIdAndRegistryNumber(
                userId,
                citizenIDCardInfo.getRegnum());
        if (userCitizenData == null) {
            userCitizenData = new UserCitizenData();
            userCitizenData.setUserId(userId);
            userCitizenData.setRegistryNumber(citizenIDCardInfo.getRegnum().toLowerCase());
            userCitizenData.setCreatedDate(LocalDateTime.now());
        }

        userCitizenData.setPersonId(citizenIDCardInfo.getPersonId());
        userCitizenData.setSurname(citizenIDCardInfo.getSurname());
        userCitizenData.setLastName(citizenIDCardInfo.getLastname());
        userCitizenData.setFirstName(citizenIDCardInfo.getFirstname());
        userCitizenData.setGender(citizenIDCardInfo.getGender());
        // if (citizenIDCardInfo.getGender() != null) {
        // if (citizenIDCardInfo.getGender().equals("Эрэгтэй")) {
        // userCitizenData.setGender(Gender.MALE);
        // } else {
        // userCitizenData.setGender(Gender.FEMALE);
        // }
        // }
        userCitizenData.setNationality(citizenIDCardInfo.getNationality());
        if (citizenIDCardInfo.getBirthDate() != null) {
            userCitizenData.setBirthDate(DateUtil.toLocalDate(citizenIDCardInfo.getBirthDate()));
        }
        userCitizenData.setBirthDateAsText(citizenIDCardInfo.getBirthDateAsText());
        userCitizenData.setBirthPlace(citizenIDCardInfo.getBirthPlace());
        userCitizenData.setImage(citizenIDCardInfo.getImage());
        userCitizenData.setPassportIssueDate(citizenIDCardInfo.getPassportIssueDate());
        userCitizenData.setPassportExpireDate(citizenIDCardInfo.getPassportExpireDate());
        userCitizenData.setPassportAddress(citizenIDCardInfo.getPassportAddress());
        userCitizenData.setAimagCityCode(citizenIDCardInfo.getAimagCityCode());
        userCitizenData.setAimagCityName(citizenIDCardInfo.getAimagCityName());
        userCitizenData.setSoumDistrictCode(citizenIDCardInfo.getSoumDistrictCode());
        userCitizenData.setSoumDistrictName(citizenIDCardInfo.getSoumDistrictName());
        userCitizenData.setBagKhorooCode(citizenIDCardInfo.getBagKhorooCode());
        userCitizenData.setBagKhorooName(citizenIDCardInfo.getBagKhorooName());
        userCitizenData.setAddressRegionName(citizenIDCardInfo.getAddressRegionName());
        userCitizenData.setAddressStreetName(citizenIDCardInfo.getAddressStreetName());
        userCitizenData.setAddressTownName(citizenIDCardInfo.getAddressTownName());
        userCitizenData.setAddressApartmentName(citizenIDCardInfo.getAddressApartmentName());
        userCitizenData.setAddressDetail(citizenIDCardInfo.getAddressDetail());

        userCitizenData.setModifiedDate(LocalDateTime.now());
        userCitizenDataRepository.save(userCitizenData);

        // notify
        try {
            notificationRepository.save(Notification.builder()
                    .title("Таны ДАН холболт амжилттай бүртгэгдлээ")
                    .message("Хэрэглэгч таны ДАН холболт амжилттай бүртгэгдлээ")
                    .status(NotificationStatus.UNREAD)
                    .relatedDataType(NotificationRelatedDataType.USER_2FA)
                    .relatedDataId(userId)
                    .build());
        } catch (Exception e) {
            log.warn("notify error: ", e.getMessage());
        }

        User user = userRepository.findByIdAndDeletedFalse(userId);
        if (user != null) {
            user.setRegistryNumber(citizenIDCardInfo.getRegnum().toLowerCase());
            user.setModifiedDate(LocalDateTime.now());
            userRepository.save(user);
        }
    }
}
