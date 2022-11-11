package com.ZenPack.interceptor;

import java.io.Serializable;
import java.util.Date;
import java.util.function.Function;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ZenPack.config.RedisUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class JwtTokenUtil implements Serializable {

	private static final String SIGNING_KEY = "zenfra_migration";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    
    @Autowired
	private RedisUtil<JSONObject> redisUtil;

	private static final long serialVersionUID = 1L;

	public String getUsernameFromToken(String token) {
		token = token.replaceAll(TOKEN_PREFIX, "");
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(SIGNING_KEY).parseClaimsJws(token).getBody();
	}

	public Boolean validateToken(String token) {
		token = token.replaceAll(TOKEN_PREFIX, "");
		boolean toRet = false;
		toRet = true;
		try {
			getUsernameFromToken(token);
		} catch (Exception ex) {
			log.error("Token Invalid {}",ex.getMessage(), ex);
			return false;
		}
		return toRet;
	}
	
	public String getUserId(String authorisation)
	{
		try {
		authorisation = authorisation.replace(TOKEN_PREFIX,"");
		if (redisUtil.getValue(authorisation) == null) {
			throw new RuntimeException("User id is not valid or session expired!");
		}
		JSONObject userObj =  (JSONObject) redisUtil.getValue(authorisation);
		return (String) userObj.get("userId");
		} catch(Exception e) {
			return getUsernameFromToken(authorisation);
		}
	}
	
}
