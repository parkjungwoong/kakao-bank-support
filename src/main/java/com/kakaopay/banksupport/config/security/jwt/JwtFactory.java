package com.kakaopay.banksupport.config.security.jwt;

import com.kakaopay.banksupport.common.constant.ResCode;
import com.kakaopay.banksupport.common.exception.ComException;
import com.kakaopay.banksupport.config.ComSettings;
import com.kakaopay.banksupport.config.security.dto.UserContext;
import com.kakaopay.banksupport.model.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Component
public class JwtFactory {
    private ComSettings settings;

    @Autowired
    public JwtFactory(ComSettings settings) {
        this.settings = settings;
    }

    /**
     * 엑세스 토큰 발급
     * @param userInfo 유저 정보
     * @return 엑세스 토큰
     * @throws ComException ErrorCode
     */
    public JwtToken getAccessToken(UserInfo userInfo) throws ComException {
        if(StringUtils.isEmpty(userInfo.getUserNo())) {
            log.error("토크 생성시 회원 번호값은 필수");
            throw new ComException(ResCode.E005);
        }

        try {
            Claims claims = Jwts.claims().setSubject(userInfo.getUserNo());

            LocalDateTime curDT = LocalDateTime.now();

            String token = Jwts.builder()
                    .setClaims(claims)
                    .setIssuer(settings.getJwtIssuer())
                    .setIssuedAt(Date.from(curDT.atZone(ZoneId.systemDefault()).toInstant()))
                    .setExpiration(Date.from(curDT.plusMinutes(settings.getJwtAccessExpTime()).atZone(ZoneId.systemDefault()).toInstant()))
                    .signWith(SignatureAlgorithm.HS512, settings.getJwtSigningKey())
                    .compact();

            return new JwtToken(token, UserContext.create(userInfo.getUserNo(),null));
        } catch (Exception e) {
            log.error("엑세스 토큰 생성 에러",e);
            throw new ComException(ResCode.E006);
        }
    }

    /**
     * 리프레시 토큰 발급
     * @param userInfo 유저 정보
     * @return 리프레시 토큰
     * @throws ComException ErrorCode
     */
    public JwtToken getRefreshToken(UserInfo userInfo) throws ComException {
        if(StringUtils.isEmpty(userInfo.getUserNo())) {
            log.error("토크 생성시 회원 번호값은 필수");
            throw new ComException(ResCode.E005);
        }

        try {
            Claims claims = Jwts.claims().setSubject(userInfo.getUserNo());
            //임시
            claims.put("RULE","REFRESH");

            LocalDateTime curDT = LocalDateTime.now();

            String token = Jwts.builder()
                    .setClaims(claims)
                    .setIssuer(settings.getJwtIssuer())
                    .setIssuedAt(Date.from(curDT.atZone(ZoneId.systemDefault()).toInstant()))
                    .setExpiration(Date.from(curDT.plusMinutes(settings.getJwtRefreshExpTime()).atZone(ZoneId.systemDefault()).toInstant()))
                    .signWith(SignatureAlgorithm.HS512, settings.getJwtSigningKey())
                    .compact();

            return new JwtToken(token, UserContext.create(userInfo.getUserNo(),null));
        } catch (Exception e) {
            log.error("리프레시 토큰 생성 에러",e);
            throw new ComException(ResCode.E006);
        }
    }

}
