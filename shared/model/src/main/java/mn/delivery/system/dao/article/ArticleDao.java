package mn.delivery.system.dao.article;

import java.util.ArrayList;
import java.util.List;

import mn.delivery.system.model.article.Article;
import mn.delivery.system.model.article.enums.ArticleStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ArticleDao {

    private final MongoTemplate mongoTemplate;

    public long count(
            String search,
            String categoryId,
            Boolean isFeatured,
            ArticleStatus status) {
        Query query = buildQuery(
                search, categoryId, isFeatured, status,
                null);
        return mongoTemplate.count(query, Article.class);
    }

    public List<Article> list(
            String search,
            String categoryId,
            Boolean isFeatured,
            ArticleStatus status,
            Pageable pageable) {
        Query query = buildQuery(
                search, categoryId, isFeatured, status,
                pageable);
        return mongoTemplate.find(query, Article.class);
    }

    private Query buildQuery(
            String search,
            String categoryId,
            Boolean isFeatured,
            ArticleStatus status,
            Pageable pageable) {
        Query query = new Query();

        if (!ObjectUtils.isEmpty(search)) {
            List<Criteria> searchCriterias = new ArrayList<>();
            searchCriterias.add(Criteria.where("title").regex(search, "i"));
            searchCriterias.add(Criteria.where("description").regex(search, "i"));
            query.addCriteria(new Criteria().orOperator(searchCriterias));
        }

        if (!ObjectUtils.isEmpty(status)) {
            query.addCriteria(Criteria.where("status").is(status));
        }

        if (!ObjectUtils.isEmpty(categoryId)) {
            query.addCriteria(Criteria.where("categoryId").is(categoryId));
        }

        if (!ObjectUtils.isEmpty(isFeatured)) {
            query.addCriteria(Criteria.where("featured").is(isFeatured));
        }

        query.addCriteria(Criteria.where("deleted").is(false));

        if (pageable != null) {
            query.with(pageable);
        }

        return query;
    }
}
