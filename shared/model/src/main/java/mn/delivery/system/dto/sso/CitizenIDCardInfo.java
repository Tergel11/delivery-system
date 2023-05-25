package mn.delivery.system.dto.sso;

import lombok.Data;

import java.util.Date;

/**
 * @author digz6666
 */
@Data
public class CitizenIDCardInfo {

    private String personId; // added from WSDL 1.3.0

    private String civilId;
    private String regnum;
    private String surname; // ургийн овог
    private String lastname;
    private String firstname;
    private String gender;

    private String nationality;
    private Date birthDate;
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
