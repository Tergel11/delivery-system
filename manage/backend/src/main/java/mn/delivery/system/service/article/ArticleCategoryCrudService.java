package mn.delivery.system.service.article;

import java.time.LocalDateTime;

import mn.delivery.system.exception.article.ArticleException;
import mn.delivery.system.model.article.ArticleCategory;
import mn.delivery.system.repository.article.ArticleCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author digz6666
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleCategoryCrudService {
    private final ArticleCategoryRepository categoryRepository;

    @Transactional
    public ArticleCategory create(ArticleCategory requestCategory) throws ArticleException {
        ArticleCategory duplicatedCategory = categoryRepository.findByValueAndDeletedFalse(requestCategory.getValue());
        if (duplicatedCategory != null) {
            throw new ArticleException("Утга давхардаж байна " + requestCategory.getValue());
        }

        ArticleCategory category = categoryRepository.save(requestCategory);
        if (category == null) {
            throw new ArticleException("Мэдээллийг үүсгэх үед алдаа гарлаа");
        }

        return category;
    }

    @Transactional
    public ArticleCategory update(ArticleCategory requestCategory) throws ArticleException {

        ArticleCategory oldCategory = categoryRepository.findByIdAndDeletedFalse(requestCategory.getId());
        if (oldCategory == null) {
            throw new ArticleException("Засварлах өгөгдөл олдсонгүй " + requestCategory.getId());
        }

        ArticleCategory duplicatedCategory = categoryRepository.findByValueAndDeletedFalse(requestCategory.getValue());
        if (duplicatedCategory != null) {
            throw new ArticleException("Утга давхардаж байна " + requestCategory.getValue());
        }

        oldCategory.setValue(requestCategory.getValue());

        return categoryRepository.save(oldCategory);
    }

    @Transactional
    public ArticleCategory delete(String categoryId, String adminId) throws ArticleException {

        ArticleCategory category = categoryRepository.findByIdAndDeletedFalse(categoryId);
        if (category == null) {
            throw new ArticleException("Устгах өгөгдөл олдсонгүй " + categoryId);
        }

        category.setDeleted(true);
        category.setModifiedBy(adminId);
        category.setModifiedDate(LocalDateTime.now());

        return categoryRepository.save(category);
    }
}
