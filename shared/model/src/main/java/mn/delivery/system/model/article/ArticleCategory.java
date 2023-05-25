package mn.delivery.system.model.article;

import jakarta.validation.constraints.NotBlank;

import mn.delivery.system.model.BaseEntityWithUser;
import org.springframework.data.mongodb.core.mapping.Sharded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Sharded(shardKey = { "id" })
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCategory extends BaseEntityWithUser {
    @NotBlank
    private String value;
}
