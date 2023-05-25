package mn.delivery.system.model.enums;

import lombok.Getter;

@Getter
public enum SharedStatus {
    CREATED("Үүсгэсэн"),
    UPCOMING("Тун удахгүй"),
    ONGOING("Үргэлжилж байна"),
    ACTIVE("Идэвхитэй"),
    FINISHED("Дууссан");

    final String name;

    SharedStatus(String name) {
        this.name = name;
    }

    public static SharedStatus fromString(String value) {
        try {
            return SharedStatus.valueOf(value);
        } catch (Exception e) {
            return null;
        }
    }
}

