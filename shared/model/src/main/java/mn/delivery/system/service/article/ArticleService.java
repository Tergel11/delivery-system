package mn.delivery.system.service.article;

import mn.delivery.system.model.article.Article;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.exception.article.ArticleException;
import mn.delivery.system.repository.article.ArticleCategoryRepository;
import mn.delivery.system.repository.article.ArticleRepository;
import mn.delivery.system.repository.auth.UserRepository;

/**
 * @author digz6666
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleCategoryRepository categoryRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public Article get(String id) throws ArticleException {
        Article article = articleRepository.findByIdAndDeletedFalse(id);
        if (article == null) {
            throw new ArticleException("Өгөгдөл олдсонгүй");
        }

        return fillData(article);
    }

    public Article fillData(Article article) {
        if (ObjectUtils.isEmpty(article)) {
            return article;
        }

        // fill category
        article.setCategory(categoryRepository.findByIdAndDeletedFalse(article.getCategoryId()));

        // fill publisher
        article.setPublisher(userRepository.findByIdAndDeletedFalse(article.getCreatedBy()));

        return article;
    }

}
