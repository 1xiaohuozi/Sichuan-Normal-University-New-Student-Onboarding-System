package com.example.sys_newwelcome.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@Component
/**
 * 将application.yml中itbluebox属性注入
 */
@ConfigurationProperties(prefix = "itbluebox.jwt")
public class JwtUtils {
    private long expire;
    private String secret;
    private String header;
    /**
     * 生成JWT
     */
    public String generateToken(String username){
        /**
         * 当前日期
         */
        Date nowDate = new Date();
        /**
         * 设置逾期时间
         */
        Date expireDate = new Date(nowDate.getTime() + 1000 * expire);
        return Jwts.builder()
                /**
                 * 设置JWT的头部参数，指定令牌类型为JWT
                 */
                .setHeaderParam("typ","JWT")
                /**
                 * 设置JWT主题，用户的标识信息
                 */
                .setSubject(username)
                /**
                 * 设置JWT的生成时间
                 */
                .setIssuedAt(nowDate)
                /**
                 * 设置JWT过期时间
                 */
                .setExpiration(expireDate)
                /**
                 * 使用E512算法和指定secret密钥进行签名
                 * 确保其完整性和防止篡改
                 */
                .signWith(SignatureAlgorithm.HS512,secret)
                /**
                 * 构建并返回最终JWT字符串
                 */
                .compact();
    }

    /**
     * 验证并解析JWT
     */
    public Claims getClaimByToken(String jwt){
        try{
            /**
             * 创建一个JWT解析器对象，用于验证和解析JWT
             */
            return Jwts.parser()
                    /**
                     * 设置解析器的密钥
                     */
                    .setSigningKey(secret)
                    /**
                     * 解析JWT签名有效性，若有效则返回一个Jws<Claims>对象，其中包含JWT声明
                     */
                    .parseClaimsJws(jwt)
                    /**
                     * 获取声明信息，用户名，过期时间等等
                     */
                    .getBody();
            /**
             * 如果JWT无效或者解析失败返回null
              */
        }catch (Exception e){
            return null;
        }
    }

    /**
     * JWT是否过期
     */
    public boolean isTokenExpired(Claims claims){
                /**
                 * 获取JWT过期时间
                 */
        return claims.getExpiration()
                /**
                 * 检查JWT过期时间是否早于现在的时间
                 * 返回true说明令牌已经过期
                 */
                .before(new Date());
    }
}
