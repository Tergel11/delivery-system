package mn.delivery.system.model.auth;

import lombok.*;
import mn.delivery.system.model.BaseEntity;
import org.springframework.data.mongodb.core.mapping.Sharded;

import java.time.LocalDateTime;

@Sharded(shardKey = { "userId", "registryNumber" })
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCitizenData extends BaseEntity {

    private String userId;
    private String registryNumber;

    private String personId; // added from WSDL 1.3.0
    private String civilId;
    private String surname; // ургийн овог
    private String lastName;
    private String firstName;
//    private Gender gender;
    private String gender;

    private String nationality;
    private LocalDateTime birthDate;
    private String birthDateAsText;
    private String birthPlace;

    private String image; // base64Binary
    private String passportIssueDate; // иргэний үнэмжлэх олгосон огноо
    private String passportExpireDate; // иргэний үнэмжлэх дуусах огноо
    private String passportAddress; // иргэний үнэмлэх дээрх хаяг

    // address
    private String aimagCityCode;
    private String aimagCityName;
    private String soumDistrictCode;
    private String soumDistrictName;
    private String bagKhorooCode;
    private String bagKhorooName;
    private String addressRegionName; // хорооллын нэр
    private String addressStreetName;
    private String addressTownName;
    private String addressApartmentName;
    private String addressDetail;
}
