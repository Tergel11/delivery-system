package mn.delivery.system.api.article;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import mn.delivery.system.service.article.ArticleCrudService;
import mn.delivery.system.api.BaseController;
import mn.delivery.system.api.request.antd.AntdPagination;
import mn.delivery.system.api.response.antd.AntdTableDataList;
import mn.delivery.system.dao.article.ArticleDao;
import mn.delivery.system.exception.article.ArticleException;
import mn.delivery.system.model.article.Article;
import mn.delivery.system.model.article.enums.ArticleStatus;
import mn.delivery.system.service.article.ArticleService;
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
@RequestMapping("/v1/article")
@RequiredArgsConstructor
public class ArticleApi extends BaseController {
    private final ArticleCrudService articleCrudService;
    private final ArticleService articleService;

    private final ArticleDao articleDao;

    @Secured("ROLE_ARTICLE_MANAGE")
    @PostMapping("create")
    public ResponseEntity<?> create(@Valid @RequestBody Article requestArticle, Principal principal) {
        try {
            String adminId = basePermissionService.getUserId(principal);

            requestArticle.setCreatedBy(adminId);
            requestArticle.setCreatedDate(LocalDateTime.now());

            return ResponseEntity.ok(articleCrudService.create(requestArticle));
        } catch (ArticleException e) {
            log.error(e.getMessage());
            return badRequest(e.getMessage());
        }
    }

    @Secured("ROLE_ARTICLE_MANAGE")
    @PutMapping("update")
    public ResponseEntity<?> update(@Valid @RequestBody Article requestArticle, Principal principal) {
        try {
            String adminId = basePermissionService.getUserId(principal);

            requestArticle.setModifiedBy(adminId);
            requestArticle.setModifiedDate(LocalDateTime.now());

            return ResponseEntity.ok(articleCrudService.update(requestArticle));
        } catch (ArticleException e) {
            log.error(e.getMessage());
            return badRequest(e.getMessage());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> get(@PathVariable String id, Principal principal) {
        try {
            return ResponseEntity.ok(articleService.get(id));
        } catch (ArticleException e) {
            log.error(e.getMessage());
            return badRequest(e.getMessage());
        }
    }

    @Secured({ "ROLE_ARTICLE_VIEW", "ROLE_ARTICLE_MANAGE" })
    @GetMapping("")
    public ResponseEntity<?> list(
            String search,
            String categoryId,
            Boolean isFeatured,
            ArticleStatus status,
            AntdPagination pagination,
            Principal principal) {
        AntdTableDataList<Article> dataList = new AntdTableDataList<>();

        try {
            pagination.setTotal(articleDao.count(search, categoryId, isFeatured, status));
            dataList.setPagination(pagination);
            List<Article> articleList = articleDao.list(search, categoryId, isFeatured, status,
                    pagination.toPageRequest());

            dataList.setList(articleList.stream().map(article -> {
                return articleService.fillData(article);
            }).collect(Collectors.toList()));
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

        return ResponseEntity.ok(dataList);
    };

    @GetMapping("published")
    public ResponseEntity<?> publishedList(
            String search,
            String categoryId,
            Boolean isFeatured,
            AntdPagination pagination,
            Principal principal) {
        AntdTableDataList<Article> dataList = new AntdTableDataList<>();

        try {
            pagination.setTotal(articleDao.count(search, categoryId, isFeatured, ArticleStatus.PUBLISHED));
            dataList.setPagination(pagination);
            List<Article> articleList = articleDao.list(search, categoryId, isFeatured, ArticleStatus.PUBLISHED,
                    pagination.toPageRequest());

            dataList.setList(articleList.stream().map(article -> {
                return articleService.fillData(article);
            }).collect(Collectors.toList()));
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

        return ResponseEntity.ok(dataList);
    };

    @Secured("ROLE_ARTICLE_MANAGE")
    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable String articleId, Principal principal) {
        try {
            String adminId = basePermissionService.getUserId(principal);

            return ResponseEntity.ok(articleCrudService.delete(articleId, adminId));
        } catch (ArticleException e) {
            log.error(e.getMessage());
            return badRequest(e.getMessage());
        }
    }

}
