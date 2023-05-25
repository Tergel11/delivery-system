package mn.delivery.system.dao.analytics;

import lombok.RequiredArgsConstructor;
import mn.delivery.system.model.analytics.dto.SumData;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

/**
 * @author digz6666
 */
@Repository
@RequiredArgsConstructor
public class SessionCountDao {

    private final MongoTemplate mongoTemplate;

    public long getSessionCount(LocalDate startDate, LocalDate endDate) {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(
                        new Criteria().and("date").gt(startDate).lte(endDate)
                ),
                Aggregation.group().sum("count").as("total")
        );

        AggregationResults<SumData> aggResult = mongoTemplate.aggregate(
                agg,
                "sessionCount",
                SumData.class);
        if (aggResult.getMappedResults().size() > 0) {
            SumData sumData = aggResult.getMappedResults().get(0);
            return sumData.getTotal();
        } else {
            return 0;
        }
    }

    public long getTotalSessionCount() {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.group().sum("count").as("total")
        );

        AggregationResults<SumData> aggResult = mongoTemplate.aggregate(
                agg,
                "sessionCount",
                SumData.class);
        SumData sumData = aggResult.getMappedResults().get(0);
        return sumData.getTotal();
    }
}
