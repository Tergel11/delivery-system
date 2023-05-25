package mn.delivery.system.location;

import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.model.location.Location;
import mn.delivery.system.repository.location.LocationRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@Disabled
@Slf4j
@SpringBootTest
//@ImportAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
public class LocationFetchServiceTest {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationFetchService locationFetchService;

    @Test
    public void testFetch() {
        List<Location> cities = locationFetchService.fetchCities();
        for (Location city : cities) {
            if (!locationRepository.existsById(city.getCode())) {
                city.setCreatedDate(LocalDateTime.now());
                locationRepository.save(city);
            }

            List<Location> districts = locationFetchService.fetchDistricts(city.getXypCode());
            for (Location district : districts) {
                if (!locationRepository.existsById(district.getCode())) {
                    district.setCreatedDate(LocalDateTime.now());
                    locationRepository.save(district);
                }

                List<Location> quarters = locationFetchService.fetchQuarters(city.getXypCode(), district.getXypCode());
                for (Location quarter : quarters) {
                    if (!locationRepository.existsById(quarter.getCode())) {
                        quarter.setCreatedDate(LocalDateTime.now());
                        locationRepository.save(quarter);
                    }
                }
            }
        }
    }
}
