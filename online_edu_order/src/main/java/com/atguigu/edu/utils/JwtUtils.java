package com.atguigu.edu.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class JwtUtils {
	public static final String SUBJECT = "guli";
	//秘钥
	public static final String APPSECRET = "guli";
	public static final long EXPIRE = 1000 * 60 * 30;  //过期时间，毫秒，30分钟

	/**
	 * 根据token，获取token里面主体内容
	 * @param request
	 * @return
	 */
	public static String getMemberIdByJwtToken(HttpServletRequest request) {
		String jwtToken = request.getHeader("token");
		if(StringUtils.isEmpty(jwtToken)) return "";

		Claims claims = Jwts.parser().setSigningKey(APPSECRET).parseClaimsJws(jwtToken).getBody();
//        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
//        Claims claims = claimsJws.getBody();
		return (String)claims.get("id");
	}


}
