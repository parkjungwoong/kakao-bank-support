package com.kakaopay.banksupport.config.security.jwt;

import com.kakaopay.banksupport.common.constant.ResCode;
import com.kakaopay.banksupport.config.security.exception.ComAuthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtParser {

    public static Jws<Claims> parseToken(final String rowToken, final String signingKey) throws ComAuthException {
        try {
            return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(rowToken);
        } catch (ExpiredJwtException exp) {
            throw new ComAuthException(ResCode.E008);
        } catch (Exception e) {
            log.error("토큰 파싱 에러",e);
            throw new ComAuthException(ResCode.E007);
        }
    }
}
