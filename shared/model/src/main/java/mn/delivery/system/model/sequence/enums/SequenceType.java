package mn.delivery.system.model.sequence.enums;

import lombok.Getter;

@Getter
public enum SequenceType {

    PAYMENT("PAYMENT", "P"), // төлбөрийн sequence
    ;

    final String value;
    final String prefix;

    SequenceType(String value, String prefix) {
        this.value = value;
        this.prefix = prefix;
    }

    public static SequenceType fromString(String input) {
        SequenceType type = null;
        try {
            type = SequenceType.valueOf(input);
        } catch (NullPointerException | IllegalArgumentException e) {
        }
        return type;
    }

    @Override
    public String toString() {
        return value;
    }
}
