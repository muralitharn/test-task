package customerServices.softsuave.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Objects;

public class JwtAuthorizationFilter extends  BasicAuthenticationFilter{
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }
    public final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        if (Objects.isNull(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            LOG.info(request.toString()+request);
            return;
        }

        Authentication authenticationTab = SecurityContextHolder.getContext().getAuthentication();
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        logger.info(token);
        UsernamePasswordAuthenticationToken authentication = null;
        if (Objects.nonNull(token)) {
            String username = jwtUtil.extractUsernameFromToken(token.replace("Bearer ", ""));
            LOG.info(username);
            if (Objects.nonNull(username)) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtUtil.validateToken(token.replace("Bearer ", ""), userDetails)) {
                    authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                }
            }
        }
        return authentication;
    }

}
