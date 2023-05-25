package mn.delivery.system.api.request.antd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * @author MethoD
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AntdPagination {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 100;

    private int currentPage;
    private int pageSize;
    private long total;

    private Sort.Direction sortDirection = Sort.Direction.DESC;
    private String sortParam = "id";

    public int getCurrentPage() {
        return currentPage == 0 ? 0 : currentPage - 1;
    }

    //  Ant Design table-д ашиглана
    public int getCurrent() {
        return currentPage == 0 ? 1 : currentPage;
    }

    public int getPageSize() {
        if (pageSize > MAX_PAGE_SIZE) {
            return MAX_PAGE_SIZE;
        }

        return pageSize != 0 ? pageSize : DEFAULT_PAGE_SIZE;
    }

    public Sort.Direction getSortDirection() {
        return sortDirection != null ? sortDirection : Sort.Direction.DESC;
    }

//    public PageRequest toPageRequestWithoutSort() {
//        return PageRequest.of(getCurrentPage(), getPageSize());
//    }

//    public PageRequest toPageRequestWithSingleSort(Sort.Direction direction, String sortParam) {
//        Sort sortWithDirection = Sort.by(direction, sortParam);
//        return PageRequest.of(getCurrentPage(), getPageSize(), sortWithDirection);
//    }

    public PageRequest toPageRequest(Sort.Order... sorts) {
        return PageRequest.of(getCurrentPage(), getPageSize(), Sort.by(sorts));
    }

    public PageRequest toPageRequest(Sort.Direction direction, String... sortParams) {
        return PageRequest.of(getCurrentPage(), getPageSize(), direction, sortParams);
    }

    public PageRequest toPageRequest() {
        return PageRequest.of(getCurrentPage(), getPageSize(), getSortDirection(), getSortParam());
    }
}
