package mn.delivery.system.dto;

import lombok.Data;

import java.util.List;

@Data
public class IndexDto {

    private String collection;
    private String field;
    private List<String> fields;
    private boolean unique;

    public IndexDto(String collection, String field) {
        this.collection = collection;
        this.field = field;
    }

    public IndexDto(String collection, String field, boolean unique) {
        this.collection = collection;
        this.field = field;
        this.unique = unique;
    }

    public IndexDto(String collection, List<String> fields, boolean unique) {
        this.collection = collection;
        this.fields = fields;
        this.unique = unique;
    }
}
