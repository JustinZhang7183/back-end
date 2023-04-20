package com.justin.backend.authorizationserver.controller;

import com.justin.backend.authorizationserver.service.JpaOAuth2AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {
    @Autowired
    private JpaOAuth2AuthorizationService jpaOAuth2AuthorizationService;

    @RequestMapping("/hello")
    public String hello() {
        return "hello!";
    }

    @RequestMapping("/test")
    public void test() {
        jpaOAuth2AuthorizationService.parseMap("{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"java.security.Principal\":{\"@class\":\"org.springframework.security.authentication.UsernamePasswordAuthenticationToken\",\"authorities\":[\"java.util.Collections$UnmodifiableRandomAccessList\",[{\"@class\":\"org.springframework.security.core.authority.SimpleGrantedAuthority\",\"authority\":\"ROLE_USER\"}]],\"details\":{\"@class\":\"org.springframework.security.web.authentication.WebAuthenticationDetails\",\"remoteAddress\":\"0:0:0:0:0:0:0:1\",\"sessionId\":\"9A9FF22C6234B000B45B49E4F8FA49B7\"},\"authenticated\":true,\"principal\":{\"@class\":\"com.justin.backend.authorizationserver.model.SecurityUser\",\"user\":{\"@class\":\"com.justin.backend.authorizationserver.entities.CustomUsers\",\"id\":3,\"username\":\"User\",\"password\":\"$2a$12$2yOChyhSuJm/naTBUjGZb.6d6mu1NsXS8XWRFousQfRTwzy0ZQtWW\",\"firstName\":null,\"lastName\":null,\"fullName\":null,\"emailAddress\":null,\"birthDate\":null,\"customAuthorities\":[\"org.hibernate.collection.spi.PersistentSet\",[{\"@class\":\"com.justin.backend.authorizationserver.entities.CustomAuthorities\",\"id\":1,\"authority\":\"ROLE_USER\"}]],\"accountNonExpired\":true,\"accountNonLocked\":true,\"credentialsNonExpired\":true,\"enabled\":true},\"enabled\":true,\"username\":\"User\",\"authorities\":[\"java.util.HashSet\",[{\"@class\":\"org.springframework.security.core.authority.SimpleGrantedAuthority\",\"authority\":\"ROLE_USER\"}]],\"password\":\"$2a$12$2yOChyhSuJm/naTBUjGZb.6d6mu1NsXS8XWRFousQfRTwzy0ZQtWW\",\"accountNonExpired\":true,\"credentialsNonExpired\":true,\"accountNonLocked\":true},\"credentials\":null},\"org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest\":{\"@class\":\"org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest\",\"authorizationUri\":\"http://localhost:8000/oauth2/authorize\",\"authorizationGrantType\":{\"value\":\"authorization_code\"},\"responseType\":{\"value\":\"code\"},\"clientId\":\"client\",\"redirectUri\":\"http://127.0.0.1:8081/login/oauth2/code/client\",\"scopes\":[\"java.util.Collections$UnmodifiableSet\",[\"read\",\"openid\"]],\"state\":null,\"additionalParameters\":{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"code_challenge_method\":\"S256\",\"continue\":\"\",\"code_challenge\":\"jUuSJNmQ2bW6QA1fYrAC2JMDL7Q4rAiKG81wZn8i1X8\"},\"authorizationRequestUri\":\"http://localhost:8000/oauth2/authorize?response_type=code&client_id=client&scope=read%20openid&redirect_uri=http://127.0.0.1:8081/login/oauth2/code/client&code_challenge_method=S256&continue=&code_challenge=jUuSJNmQ2bW6QA1fYrAC2JMDL7Q4rAiKG81wZn8i1X8\",\"attributes\":{\"@class\":\"java.util.Collections$UnmodifiableMap\"}},\"state\":\"zo_Ifqm4EimRMft-czIVwC3Oq1xi0JDt4lCsmtOUvcQ=\"}");
    }
}
