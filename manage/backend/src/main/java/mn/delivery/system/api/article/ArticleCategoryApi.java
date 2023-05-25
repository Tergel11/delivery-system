package mn.delivery.system.api.article;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;

import mn.delivery.system.service.article.ArticleCategoryCrudService;
import mn.delivery.system.api.BaseController;
import mn.delivery.system.exception.article.ArticleException;
import mn.delivery.system.model.article.ArticleCategory;
import mn.delivery.system.repository.article.ArticleCategoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/article-category")
@RequiredArgsConstructor
public class ArticleCategoryApi extends BaseController {
    private final ArticleCategoryCrudService categoryCrudService;
    private final ArticleCategoryRepository categoryRepository;

    @Secured("ROLE_ARTICLE_MANAGE")
    @PostMapping("create")
    public ResponseEntity<?> create(@Valid @RequestBody ArticleCategory requestCategory, Principal principal) {
        try {
            String adminId = basePermissionService.getUserId(principal);

            requestCategory.setCreatedBy(adminId);
            requestCategory.setCreatedDate(LocalDateTime.now());

            return ResponseEntity.ok(categoryCrudService.create(requestCategory));
        } catch (ArticleException e) {
            log.error(e.getMessage());
            return badRequest(e.getMessage());
        }
    }

    @Secured("ROLE_ARTICLE_MANAGE")
    @PutMapping("update")
    public ResponseEntity<?> update(@Valid @RequestBody ArticleCategory requestCategory, Principal principal) {
        try {
            String adminId = basePermissionService.getUserId(principal);

            requestCategory.setModifiedBy(adminId);
            requestCategory.setModifiedDate(LocalDateTime.now());

            return ResponseEntity.ok(categoryCrudService.update(requestCategory));
        } catch (ArticleException e) {
            log.error(e.getMessage());
            return badRequest(e.getMessage());
        }
    }

    @GetMapping("select")
    public List<?> select() {
        return categoryRepository.findAllDeletedFalse();
    };

    @Secured("ROLE_ARTICLE_MANAGE")
    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable String id, Principal principal) {
        try {
            String adminId = basePermissionService.getUserId(principal);

            return ResponseEntity.ok(categoryCrudService.delete(id, adminId));
        } catch (ArticleException e) {
            log.error(e.getMessage());
            return badRequest(e.getMessage());
        }
    }
}
