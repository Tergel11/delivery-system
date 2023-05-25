package mn.delivery.system.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import mn.delivery.system.model.auth.BusinessRole;
import mn.delivery.system.model.auth.enums.ApplicationRole;
import mn.delivery.system.exception.auth.BusinessRoleException;
import mn.delivery.system.exception.model.ErrorMessage;
import mn.delivery.system.repository.auth.BusinessRoleRepository;
import mn.delivery.system.repository.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author digz6666
 */
@Slf4j
@Service("jwtUserDetailsService")
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BusinessRoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(final String email) throws AuthenticationException {
        mn.delivery.system.model.auth.User user = userRepository.findByEmailAndDeletedFalse(email);
        if (user == null)
            throw new UsernameNotFoundException(ErrorMessage.USERNAME_NOTFOUND.getValue());

        if (!user.isActive())
            throw new DisabledException(ErrorMessage.DISABLED.getValue());

        if (ObjectUtils.isEmpty(user.getRole()))
            throw new BusinessRoleException(ErrorMessage.BUSINESS_ROLE_NOTFOUND.getValue());

        Optional<BusinessRole> businessRoleOptional = roleRepository.findById(user.getRole());
        if (businessRoleOptional.isPresent()) {
            BusinessRole businessRole = businessRoleOptional.get();
            List<ApplicationRole> userRoles = businessRole.getApplicationRoles().stream()
                    .filter(Objects::nonNull)
                    .toList();

            List<GrantedAuthority> authorities = userRoles.stream()
                    .filter(Objects::nonNull)
                    .map(r -> new SimpleGrantedAuthority(r.getValue()))
                    .collect(Collectors.toList());
            return new User(
                    user.getEmail(),
                    user.getPassword() != null ? user.getPassword() : "",
                    user.isActive(),
                    true,
                    true,
                    true,
                    authorities);
        } else
            throw new BusinessRoleException(ErrorMessage.BUSINESS_ROLE_NOTFOUND.getValue());
    }
}
