package mn.delivery.system.repository.article;

import java.util.List;

import mn.delivery.system.model.article.Article;
import org.springframework.data.mongodb.core.mapping.Unwrapped.Nullable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author digz6666
 */
@Repository
public interface ArticleRepository extends MongoRepository<Article, String> {
    @Query(value = "{'categoryId': ?0, 'deleted': false}")
    List<Article> findAllByCategoryId(String categoryId);

    @Query(value = "{'status': ?0, 'deleted': false}")
    List<Article> findAllByStatus(String status);

    @Query(value = "{'categoryId': ?0, 'status': ?1, 'deleted': false}")
    List<Article> findAllByCategoryIdAndStatus(String categoryId, String status);

    @Nullable
    Article findByIdAndDeletedFalse(String id);

    @Nullable
    Article findByTitleAndDeletedFalse(String title);
}
