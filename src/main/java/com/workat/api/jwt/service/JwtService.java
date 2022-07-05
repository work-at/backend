package com.workat.api.jwt.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class JwtService {

	@Value("${jwt.secret}")
	private String SECRET_KEY;

	public Claims createClaims(HashMap map) {
		return Jwts.claims(map);
	}

	public String createToken(Claims claims, Long validationTime) {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		return Jwts.builder()
				   .setClaims(claims)
				   .setIssuedAt(new Date(System.currentTimeMillis()))
				   .setExpiration(new Date(System.currentTimeMillis() + validationTime))
				   .signWith(createSecretKey(signatureAlgorithm.getJcaName()), signatureAlgorithm)
				   .compact();
	}

	private Key createSecretKey(String algorithm) {
		return new SecretKeySpec(
			DatatypeConverter.parseBase64Binary(SECRET_KEY), // secret key bytes
			algorithm); // algorithm
	}

	private Claims extractClaims(String token) throws ExpiredJwtException {
		return Jwts.parserBuilder()
				   .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
				   .build()
				   .parseClaimsJws(token)
				   .getBody();
	}

	public String getValueFromJWT(String token, String key) {
		return extractClaims(token).get(key, String.class);
	}

	public <T> T getValueFromJWT(String token, String key, Class<T> requiredType) {
		return extractClaims(token).get(key, requiredType);
	}

	public boolean isTokenExpired(String token) {
		try {
			Date expiration = extractClaims(token).getExpiration();
			return expiration.before(new Date());
		} catch (ExpiredJwtException e) {
			return true;
		}
	}

}
