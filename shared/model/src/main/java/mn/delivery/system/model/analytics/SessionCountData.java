package mn.delivery.system.model.analytics;

import lombok.Data;

/**
 * @author digz6666
 */
@Data
public class SessionCountData {
    private long today;
    private long thisWeek;
    private long thisMonth;
    private long total;
}
