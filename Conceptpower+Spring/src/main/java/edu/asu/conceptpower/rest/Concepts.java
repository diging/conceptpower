package edu.asu.conceptpower.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class Concepts {
	
	@Autowired
	private AuthenticationProvider authProvider;

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "rest/concept/add", method = RequestMethod.POST)
	public ResponseEntity<String> addConcept(@RequestBody String body) {
		System.out.println(body);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
