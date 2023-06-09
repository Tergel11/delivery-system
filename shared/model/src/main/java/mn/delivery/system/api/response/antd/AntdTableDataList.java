package mn.delivery.system.api.response.antd;

import lombok.Data;
import mn.delivery.system.api.request.antd.AntdPagination;

/**
 * @param <T>
 * @author MethoD
 */
@Data
public class AntdTableDataList<T> {

    private AntdPagination pagination;
    private Iterable<T> list;
}
