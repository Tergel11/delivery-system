package mn.delivery.system.service.article;

import mn.delivery.system.model.article.ArticleCategory;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.exception.article.ArticleException;
import mn.delivery.system.repository.article.ArticleCategoryRepository;

/**
 * @author digz6666
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleCategoryService {
    private final ArticleCategoryRepository categoryRepository;

    public ArticleCategory get(String id) throws ArticleException {
        ArticleCategory category = categoryRepository.findByIdAndDeletedFalse(id);
        if (category == null) {
            throw new ArticleException("Өгөгдөл олдсонгүй");
        }

        return category;
    }
}
