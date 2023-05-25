package mn.delivery.system.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.util.JwtTokenUtil;
import mn.delivery.system.service.AuthService;
import mn.delivery.system.service.ErrorMessageService;
import mn.delivery.system.exception.auth.AuthorizationException;
import mn.delivery.system.exception.model.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author digz6666
 */
@Slf4j
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {

    @Value("${token.header}")
    private String tokenHeader;

    @Autowired
    private AuthService authService;
    @Autowired
    private ErrorMessageService messageService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain
    ) throws ServletException, IOException {
        String authToken = request.getHeader(tokenHeader);
        if (ObjectUtils.isEmpty(authToken)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            if (jwtTokenUtil.validateToken(authToken)) {
                String username = jwtTokenUtil.getUsernameFromToken(authToken);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                chain.doFilter(request, response);
            } else {
//                throw new AuthorizationException(messageService.get(ErrorMessage.UNAUTHORIZED));
//                throw new AuthorizationException(ErrorMessage.UNAUTHORIZED.getValue());
                throw new AuthorizationException(messageService.get(ErrorMessage.UNAUTHORIZED));
            }
        } catch (Exception e) {
//            log.error(e.getMessage(), e);
            authService.sendAuthorizationResponse(request, response, e);
        }
    }
}
