package mn.delivery.system.model.sequence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mn.delivery.system.model.sequence.enums.SequenceType;
import mn.delivery.system.model.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class SequenceNumber extends BaseEntity {

    private SequenceType type;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int sequence;

}
