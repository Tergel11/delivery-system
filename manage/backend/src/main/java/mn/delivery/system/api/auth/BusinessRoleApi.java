package mn.delivery.system.api.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.annotations.InjectUser;
import mn.delivery.system.api.request.DeleteRequest;
import mn.delivery.system.api.request.antd.AntdPagination;
import mn.delivery.system.api.response.antd.AntdTableDataList;
import mn.delivery.system.dao.auth.BusinessRoleDao;
import mn.delivery.system.model.auth.BusinessRole;
import mn.delivery.system.model.auth.User;
import mn.delivery.system.model.auth.enums.ApplicationRole;
import mn.delivery.system.repository.auth.BusinessRoleRepository;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * @author Tergel
 */
@Slf4j
@RestController
@RequestMapping("/v1/business-role")
@RequiredArgsConstructor
public class BusinessRoleApi {

    private final BusinessRoleRepository businessRoleRepository;
    private final BusinessRoleDao businessRoleDao;
    private final MessageSource messageSource;

    @GetMapping
    @Secured({"ROLE_BUSINESS_ROLE_VIEW", "ROLE_BUSINESS_ROLE_MANAGE", "ROLE_USER_VIEW", "ROLE_USER_MANAGE"})
    public ResponseEntity<?> list(String role, ApplicationRole applicationRole, List<String> accessibleRoles, Boolean permitAllRoles, AntdPagination pagination) {
        AntdTableDataList<BusinessRole> listData = new AntdTableDataList<>();

        pagination.setTotal(businessRoleDao.count(role, applicationRole,accessibleRoles, permitAllRoles));
        listData.setPagination(pagination);
        listData.setList(businessRoleDao.list(role, applicationRole, accessibleRoles, permitAllRoles,
                PageRequest.of(pagination.getCurrentPage(), pagination.getPageSize())));

        return ResponseEntity.ok(listData);
    }

    @GetMapping("select")
    @Secured({"ROLE_BUSINESS_ROLE_VIEW", "ROLE_BUSINESS_ROLE_MANAGE", "ROLE_USER_VIEW", "ROLE_USER_MANAGE"})
    public ResponseEntity<?> select(String role, ApplicationRole applicationRole, List<String> accessibleRoles, Boolean permitAllRoles) {
        return ResponseEntity.ok(businessRoleDao.list(role, applicationRole, accessibleRoles, permitAllRoles, null));
    }

    @InjectUser
    @PostMapping("create")
    @Secured("ROLE_BUSINESS_ROLE_MANAGE")
    public ResponseEntity<?> create(@RequestBody BusinessRole createRequest, User user, Locale locale) {
        if (businessRoleRepository.existsById(createRequest.getRole()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(messageSource.getMessage("data.exists", null, locale));

        log.info("Create business role: " + createRequest.getRole() + " by " + user.getEmail());
        BusinessRole businessRole = new BusinessRole();
        businessRole.setRole(createRequest.getRole());
        businessRole.setName(createRequest.getName());
        businessRole.setApplicationRoles(createRequest.getApplicationRoles());
        businessRole.setAccessibleBusinessRoles(createRequest.getAccessibleBusinessRoles());
        businessRoleRepository.save(businessRole);

        return ResponseEntity.ok(true);
    }

    @PutMapping("update")
    @Secured("ROLE_BUSINESS_ROLE_MANAGE")
    public ResponseEntity<?> update(@RequestBody BusinessRole updateRequest, Locale locale) {
        Optional<BusinessRole> businessRoleOpt = businessRoleRepository.findById(updateRequest.getKey());
        if (businessRoleOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(messageSource.getMessage("data.notFound", null, locale));

        log.debug("Update business role: " + updateRequest);
        BusinessRole businessRole = businessRoleOpt.get();
        businessRole.setName(updateRequest.getName());
        businessRole.setApplicationRoles(updateRequest.getApplicationRoles());
        businessRoleRepository.save(businessRole);

        return ResponseEntity.ok(true);
    }

    @DeleteMapping("delete")
    @Secured("ROLE_BUSINESS_ROLE_MANAGE")
    public ResponseEntity<?> deleteMulti(@RequestBody DeleteRequest deleteRequest, Locale locale) {
        if (ObjectUtils.isEmpty(deleteRequest.getId()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(messageSource.getMessage("error.invalidRequest", null, locale));

        BusinessRole businessRole = businessRoleRepository.findById(deleteRequest.getId()).orElse(null);
        if (businessRole == null)
            return ResponseEntity.badRequest().body("Цахим ажлын байр олдсонгүй: " + deleteRequest.getId());

        log.debug("Deleting business roles: " + deleteRequest);
        businessRoleRepository.deleteById(deleteRequest.getId());
        return ResponseEntity.ok(true);
    }
}
