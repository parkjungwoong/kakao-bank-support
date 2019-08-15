package com.kakaopay.banksupport.config.security.jwt;

import com.kakaopay.banksupport.config.security.dto.UserContext;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 12312312414142231L;

    private JwtToken jwtToken;

    public JwtAuthenticationToken(String rawToken) {
        super(null);
        this.jwtToken = new JwtToken(rawToken, null);
        setAuthenticated(false);
    }

    public JwtAuthenticationToken(UserContext userContext, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        jwtToken = new JwtToken(null, userContext);
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return jwtToken.getToken();
    }

    @Override
    public Object getPrincipal() {
        return jwtToken.getUserContext();
    }
}
