package mn.delivery.system.service.article;

import java.time.LocalDateTime;

import mn.delivery.system.database.util.MongoUtil;
import mn.delivery.system.service.S3BucketService;
import mn.delivery.system.dto.MoveFileRequest;
import mn.delivery.system.exception.article.ArticleException;
import mn.delivery.system.model.article.Article;
import mn.delivery.system.model.article.enums.ArticleStatus;
import mn.delivery.system.repository.article.ArticleRepository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author digz6666
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleCrudService {
    private final ArticleRepository articleRepository;

    private final S3BucketService s3BucketService;

    private final MongoUtil mongoUtil;

    @Transactional
    public Article create(Article requestArticle) throws ArticleException {
        Article duplicatedArticle = articleRepository.findByTitleAndDeletedFalse(requestArticle.getTitle());
        if (duplicatedArticle != null) {
            throw new ArticleException("Нэр давхардаж байна " + requestArticle.getTitle());
        }

        // move cover images to article folder
        String foldePath = pathParser("article/");

        // main
        MoveFileRequest mfRequestMain = MoveFileRequest.builder()
                .entity(foldePath)
                .fileUrl(requestArticle.getMainImgUrl())
                .build();
        // thumb
        MoveFileRequest mfRequestThumb = MoveFileRequest.builder()
                .entity(foldePath)
                .fileUrl(requestArticle.getThumbImgUrl())
                .build();

        try {
            requestArticle.setMainImgUrl(s3BucketService.moveTempFile(mfRequestMain));
            requestArticle.setThumbImgUrl(s3BucketService.moveTempFile(mfRequestThumb));
        } catch (Exception e) {
            log.error("Move images into article folder failed: ");
            log.error(e.getMessage());
            throw new ArticleException(
                    "Мэдээний зураг-г зөөх үед алдаа гарлаа");
        }

        if (requestArticle.getStatus().equals(ArticleStatus.PUBLISHED)) {
            requestArticle.setPublishedDate(LocalDateTime.now());
        }

        Article article = articleRepository.save(requestArticle);
        if (article == null) {
            throw new ArticleException("Мэдээллийг үүсгэх үед алдаа гарлаа");
        }

        return article;
    }

    @Transactional
    public Article update(Article requestArticle) throws ArticleException {
        Article oldArticle = articleRepository.findByIdAndDeletedFalse(requestArticle.getId());
        if (oldArticle == null) {
            throw new ArticleException("Засварлах өгөгдөл олдсонгүй " + requestArticle.getId());
        }

        if (!oldArticle.getTitle().equals(requestArticle.getTitle())) {
            Article duplicatedArticle = articleRepository.findByTitleAndDeletedFalse(requestArticle.getTitle());

            if (duplicatedArticle != null) {
                throw new ArticleException("Нэр давхардаж байна " + requestArticle.getTitle());
            }
        }
        // update collection cover images
        String coverImgPath = pathParser("article/");

        if (!oldArticle.getMainImgUrl().equals(requestArticle.getMainImgUrl())) {
            // main
            MoveFileRequest mfRequestMain = MoveFileRequest.builder()
                    .entity(coverImgPath)
                    .fileUrl(requestArticle.getMainImgUrl())
                    .build();

            try {
                if (!ObjectUtils.isEmpty(oldArticle.getMainImgUrl())
                        && oldArticle.getMainImgUrl().indexOf("article/") != -1) {
                    // Remove old image
                    s3BucketService.deleteFileIfExists(oldArticle.getMainImgUrl()
                            .substring(oldArticle.getMainImgUrl()
                                    .indexOf("article/")));
                }
                requestArticle.setMainImgUrl(s3BucketService.moveTempFile(mfRequestMain));
            } catch (Exception e) {
                log.error("Update main image of article failed: ");
                log.error(e.getMessage());
                throw new ArticleException(
                        "Article main image-г шинэчлэх үед алдаа гарлаа");
            }
        }

        if (!oldArticle.getThumbImgUrl().equals(requestArticle.getThumbImgUrl())) {
            // thumb
            MoveFileRequest mfRequestThumb = MoveFileRequest.builder()
                    .entity(coverImgPath)
                    .fileUrl(requestArticle.getThumbImgUrl())
                    .build();

            try {
                if (!ObjectUtils.isEmpty(oldArticle.getThumbImgUrl())
                        && oldArticle.getThumbImgUrl().indexOf("article/") != -1) {
                    // Remove old image
                    s3BucketService.deleteFileIfExists(oldArticle.getThumbImgUrl()
                            .substring(oldArticle.getThumbImgUrl()
                                    .indexOf("article/")));
                }
                requestArticle.setThumbImgUrl(s3BucketService.moveTempFile(mfRequestThumb));
            } catch (Exception e) {
                log.error("Update thumb image of article failed: ");
                log.error(e.getMessage());
                throw new ArticleException(
                        "Article thumb image-г шинэчлэх үед алдаа гарлаа");
            }
        }

        // set published date
        if (oldArticle.getStatus().equals(ArticleStatus.DRAFT)
                && requestArticle.getStatus().equals(ArticleStatus.PUBLISHED)) {
            requestArticle.setPublishedDate(LocalDateTime.now());
        } else {
            requestArticle.setPublishedDate(oldArticle.getPublishedDate());
        }

        log.info("Update article - " + requestArticle.getTitle());
        Article newArticle = mongoUtil.findAndModify(
                new Query()
                        .addCriteria(Criteria.where("id").is(oldArticle.getId())),
                new Update()
                        .set("categoryId", requestArticle.getCategoryId())
                        .set("title", requestArticle.getTitle())
                        .set("description", requestArticle.getDescription())
                        .set("body", requestArticle.getBody())
                        .set("status", requestArticle.getStatus())
                        .set("publishedDate", requestArticle.getPublishedDate())
                        .set("mainImgUrl", requestArticle.getMainImgUrl())
                        .set("thumbImgUrl", requestArticle.getThumbImgUrl())
                        .set("featured", requestArticle.isFeatured())
                        .set("modifiedBy", requestArticle.getModifiedBy())
                        .set("modifiedDate", requestArticle.getModifiedDate()),
                MongoUtil.FM_OPTIONS,
                Article.class);
        if (ObjectUtils.isEmpty(newArticle)) {
            throw new ArticleException("Өгөгдөл хадгалах үед алдаа гарлаа");
        }

        return newArticle;
    }

    @Transactional
    public Article delete(String articleId, String adminId) throws ArticleException {

        Article article = articleRepository.findByIdAndDeletedFalse(articleId);
        if (article == null) {
            throw new ArticleException("Устгах өгөгдөл олдсонгүй " + articleId);
        }

        article.setDeleted(true);
        article.setModifiedBy(adminId);
        article.setModifiedDate(LocalDateTime.now());

        return articleRepository.save(article);
    }

    private String pathParser(String path) {
        return path.toLowerCase().replaceAll("\\s+", "_").replaceAll("-", "_");
    }

}
