package mn.delivery.system.dto;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.ObjectUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tergel
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MoveFileRequest {

    private String fileUrl;
    private String entity;
    private String dataId;
    private String name;
    private String userId;
    private boolean copy;

    public Map<String, String> getMetaData(String newFileKey, String newName) {
        Map<String, String> map = new HashMap<>();
        map.put("key", newFileKey);
        map.put("name", newName);

        if (!ObjectUtils.isEmpty(userId))
            map.put("userId", userId);

        if (!ObjectUtils.isEmpty(entity))
            map.put("entity", entity);

        return map;
    }

    public String getFileName(String oldName) {
        return ObjectUtils.isEmpty(name) ? oldName : name;
    }

}
