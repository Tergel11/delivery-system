package mn.delivery.system.repository.article;

import java.util.List;

import mn.delivery.system.model.article.ArticleCategory;
import org.springframework.data.mongodb.core.mapping.Unwrapped.Nullable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author digz6666
 */
@Repository
public interface ArticleCategoryRepository extends MongoRepository<ArticleCategory, String> {
    @Nullable
    ArticleCategory findByIdAndDeletedFalse(String id);

    @Nullable
    ArticleCategory findByValueAndDeletedFalse(String value);

    @Query(value = "{'deleted': false}")
    List<ArticleCategory> findAllDeletedFalse();
}
