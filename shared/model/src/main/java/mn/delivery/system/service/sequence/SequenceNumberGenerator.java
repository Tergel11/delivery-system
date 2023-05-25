package mn.delivery.system.service.sequence;

import lombok.RequiredArgsConstructor;
import mn.delivery.system.model.sequence.SequenceNumber;
import mn.delivery.system.model.sequence.enums.SequenceType;
import mn.delivery.system.repository.sequence.SequenceNumberRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class SequenceNumberGenerator {
    private static final DateTimeFormatter DATE_HOUR = DateTimeFormatter.ofPattern("yyMMddHH");

    private final MongoTemplate mongoTemplate;
    private final SequenceNumberRepository sequenceNumberRepository;

    /**
     * @return number PyyMMddHH0000 (or more than 4 additional digits)
     */
    public String getPaymentNextNumber() {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        int hour = now.getHour();

        // get sequence
        SequenceNumber paymentSequence = mongoTemplate.findAndModify(
                new Query()
                        .addCriteria(Criteria.where("type").is(SequenceType.PAYMENT))
                        .addCriteria(Criteria.where("year").is(year))
                        .addCriteria(Criteria.where("month").is(month))
                        .addCriteria(Criteria.where("day").is(day))
                        .addCriteria(Criteria.where("hour").is(hour)),
                new Update().inc("sequence", 1),
                SequenceNumber.class
        );

        //log.debug("prSequence: " + prSequence);
        int sequence;
        if (paymentSequence == null) {
            sequence = 1;
            sequenceNumberRepository.save(new SequenceNumber(SequenceType.PAYMENT, year, month, day, hour, sequence));
        } else {
            sequence = paymentSequence.getSequence() + 1;
        }

        return SequenceType.PAYMENT.getPrefix() + "" + DATE_HOUR.format(now) + String.format("%04d", sequence);
    }

}
