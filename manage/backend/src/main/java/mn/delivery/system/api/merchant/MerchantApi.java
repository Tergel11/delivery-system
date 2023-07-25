package mn.delivery.system.api.merchant;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.api.BaseController;
import mn.delivery.system.api.request.antd.AntdPagination;
import mn.delivery.system.api.response.antd.AntdTableDataList;
import mn.delivery.system.dao.merchant.MerchantDao;
import mn.delivery.system.exception.article.ArticleException;
import mn.delivery.system.model.merchant.Merchant;
import mn.delivery.system.repository.merchant.MerchantRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/merchant")
@RequiredArgsConstructor
public class MerchantApi extends BaseController {

    private final MerchantDao merchantDao;
    private final MerchantRepository merchantRepository;

    @Secured({ "ROLE_MERCHANT_VIEW", "ROLE_MERCHANT_MANAGE" })
    @GetMapping("")
    public ResponseEntity<?> list(
            String userId,
            String name,
            String bundleId,
            String wareHouseId,
            AntdPagination pagination) {
        AntdTableDataList<Merchant> dataList = new AntdTableDataList<>();

        try {
            pagination.setTotal(merchantDao.count(userId, name, bundleId, wareHouseId));
            dataList.setPagination(pagination);
            List<Merchant> merchantList = merchantDao.list(userId, name, bundleId, wareHouseId, pagination.toPageRequest());

            dataList.setList(merchantList);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

        return ResponseEntity.ok(dataList);
    }

    @Secured("ROLE_MERCHANT_MANAGE")
    @PostMapping("create")
    public ResponseEntity<?> create(@Valid @RequestBody Merchant requestMerchant, Principal principal) {
        try {

            Merchant merchant = new Merchant();

            merchant.setUserId(requestMerchant.getUserId());
            Merchant duplicateMerchant =  merchantRepository.findByNameAndDeletedFalse(requestMerchant.getName());
            if (!ObjectUtils.isEmpty(duplicateMerchant))
                return ResponseEntity.badRequest().body("Нэр давхардаж байна "+ requestMerchant.getName());
            merchant.setName(requestMerchant.getName());
            merchant.setBundleId(requestMerchant.getBundleId());
            merchant.setWareHouseId(requestMerchant.getWareHouseId());

            String adminId = basePermissionService.getUserId(principal);
            merchant.setCreatedBy(adminId);
            merchant.setCreatedDate(LocalDateTime.now());

            return ResponseEntity.ok(merchant);
        } catch (ArticleException e) {
            log.error(e.getMessage());
            return badRequest(e.getMessage());
        }
    }

    @Secured("ROLE_MERCHANT_MANAGE")
    @PutMapping("update")
    public ResponseEntity<?> update(@Valid @RequestBody Merchant requestMerchant, Principal principal) {
        try {
            Merchant merchant = merchantRepository.findByIdAndDeletedFalse(requestMerchant.getId());
            if (ObjectUtils.isEmpty(merchant))
                return ResponseEntity.badRequest().body("Засварлах өгөгдөл олдсонгүй "+ requestMerchant.getId());

            merchant.setUserId(requestMerchant.getUserId());
            Merchant duplicateMerchant =  merchantRepository.findByNameAndDeletedFalse(requestMerchant.getName());
            if (!ObjectUtils.isEmpty(duplicateMerchant))
                return ResponseEntity.badRequest().body("Нэр давхардаж байна "+ requestMerchant.getName());
            merchant.setName(requestMerchant.getName());
            merchant.setBundleId(requestMerchant.getBundleId());
            merchant.setWareHouseId(requestMerchant.getWareHouseId());

            String adminId = basePermissionService.getUserId(principal);
            merchant.setModifiedBy(adminId);
            merchant.setModifiedDate(LocalDateTime.now());

            return ResponseEntity.ok(merchant);
        } catch (ArticleException e) {
            log.error(e.getMessage());
            return badRequest(e.getMessage());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> get(@PathVariable String id, Principal principal) {
        try {
            return ResponseEntity.ok(merchantRepository.findByIdAndDeletedFalse(id));
        } catch (ArticleException e) {
            log.error(e.getMessage());
            return badRequest(e.getMessage());
        }
    }

    @Secured("ROLE_MERCHANT_MANAGE")
    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable String id, Principal principal) {
        try {
            String adminId = basePermissionService.getUserId(principal);

            Merchant merchant = merchantRepository.findByIdAndDeletedFalse(id);
            if (ObjectUtils.isEmpty(merchant))
                return ResponseEntity.badRequest().body("Устгах өгөгдөл олдсонгүй "+ id);

            merchant.setDeleted(true);
            merchant.setModifiedBy(adminId);
            merchant.setModifiedDate(LocalDateTime.now());
            return ResponseEntity.ok(merchant);
        } catch (ArticleException e) {
            log.error(e.getMessage());
            return badRequest(e.getMessage());
        }
    }
}
