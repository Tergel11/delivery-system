package mn.delivery.system.util;

import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.model.BaseEntity;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tergel
 */
@Slf4j
public class ListUtils {

    public static List<String> getIds(List<? extends BaseEntity> items) {
        List<String> ids = new ArrayList<>();
        if (ObjectUtils.isEmpty(items))
            return ids;

        for (BaseEntity item : items)
            ids.add(item.getId());
        return ids;
    }
}
