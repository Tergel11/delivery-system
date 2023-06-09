package mn.delivery.system.api.response;

import lombok.Data;

/**
 * @param <T>
 * @author MethoD
 */
@Data
public class PageableListData<T> {

    private long count;
    private Iterable<T> list;
}
