package mn.delivery.system.dao.location;

import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.model.location.Location;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
//import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Disabled
@Slf4j
@SpringBootTest
//@ImportAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
public class LocationDaoTest {

    @Autowired
    private LocationDao locationDao;

    @Test
    public void testListCity() {
        List<Location> locations = locationDao.listCity();
        for (Location location : locations) {
            log.info("location -> " + location);
        }
    }
}
