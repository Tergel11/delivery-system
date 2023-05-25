package mn.delivery.system.location;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.model.location.Location;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationFetchService {

    private final String baseUrl = "https://xxx.mn/xxx/api/address";
    private final RestTemplate restTemplate = new RestTemplate();

    public List<Location> fetchCities() {
        ResponseEntity<XypResponse> response = restTemplate.getForEntity(
                baseUrl + "/city",
                XypResponse.class
        );

        List<Location> cities = new ArrayList<>();

        if (response.getStatusCode() == HttpStatus.OK && response.hasBody()) {
            Map<String, Object> data = (Map<String, Object>) response.getBody().getData();
            List<Object> listData = (List<Object>) data.get("listData");
            for (Object cityDataObj : listData) {
                Map<String, Object> cityData = (Map<String, Object>) cityDataObj;
                log.info("cityData -> " + cityData);

                String aimagCityCode = cityData.get("aimagCityCode").toString();
                String aimagCityName = cityData.get("aimagCityName").toString();

                int order = 0;
                if (aimagCityCode.equals("Улаанбаатар")) {
                    order = 1;
                }

                cities.add(Location.builder()
                        .code(aimagCityCode)
                        .xypCode(aimagCityCode)
                        .name(aimagCityName)
                        .order(order)
                        .build());
            }
        }

        return cities;
    }

    public List<Location> fetchDistricts(String cityXypCode) {
        ResponseEntity<XypResponse> response = restTemplate.getForEntity(
                baseUrl + "/district?city={city}",
                XypResponse.class,
                cityXypCode
        );

        List<Location> districts = new ArrayList<>();

        if (response.getStatusCode() == HttpStatus.OK && response.hasBody()) {
            Map<String, Object> data = (Map<String, Object>) response.getBody().getData();
            List<Object> listData = (List<Object>) data.get("listData");
            for (Object districtDataObj : listData) {
                Map<String, Object> districtData = (Map<String, Object>) districtDataObj;
                log.info("districtData -> " + districtData);

                String soumDistrictCode = districtData.get("soumDistrictCode").toString();
                String soumDistrictName = districtData.get("soumDistrictName").toString();

                districts.add(Location.builder()
                        .code(cityXypCode + soumDistrictCode)
                        .xypCode(soumDistrictCode)
                        .parentCode(cityXypCode)
                        .name(soumDistrictName)
                        .build());
            }
        }

        return districts;
    }

    public List<Location> fetchQuarters(String cityXypCode, String districtXypCode) {
        ResponseEntity<XypResponse> response = restTemplate.getForEntity(
                baseUrl + "/quarter?city={city}&district={district}",
                XypResponse.class,
                cityXypCode,
                districtXypCode
        );

        List<Location> quarters = new ArrayList<>();

        if (response.getStatusCode() == HttpStatus.OK && response.hasBody()) {
            Map<String, Object> data = (Map<String, Object>) response.getBody().getData();
            List<Object> listData = (List<Object>) data.get("listData");
            for (Object quarterDataObj : listData) {
                Map<String, Object> quarterData = (Map<String, Object>) quarterDataObj;
                log.info("quarterData -> " + quarterData);

                String bagKhorooCode = quarterData.get("bagKhorooCode").toString();
                String bagKhorooName = quarterData.get("bagKhorooName").toString();

                quarters.add(Location.builder()
                        .code(cityXypCode + districtXypCode + bagKhorooCode)
                        .xypCode(bagKhorooCode)
                        .parentCode(cityXypCode + districtXypCode)
                        .name(bagKhorooName)
                        .build());
            }
        }

        return quarters;
    }
}
