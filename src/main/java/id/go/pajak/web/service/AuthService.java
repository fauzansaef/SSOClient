package id.go.pajak.web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.go.pajak.web.model.CheckToken;
import org.springframework.security.crypto.codec.Base64;
import id.go.pajak.web.model.UserAuth;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    @Value("${auth.CheckTokenEndpoint}")
    private String cekTokenEndpoint;
    @Value("${auth.TokenEndpoint}")
    private String tokenEndpoint;
    @Value("${auth.RedirectUri}")
    private String redirectUri;
    @Value("${auth.ClientId}")
    private String clientId;
    @Value("${auth.ClientSecret}")
    private String clientSecret;
    @Value("${auth.Apphost}")
    private String apphost;

    protected final Log logger = LogFactory.getLog(getClass());

    private RestOperations restTemplate;

    public void setRestTemplate(RestOperations restTemplate) {
        this.restTemplate = restTemplate;
    }

    public AuthService() {
        restTemplate = new RestTemplate();
        ((RestTemplate) restTemplate).setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            // Ignore 400
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() != 400) {
                    super.handleError(response);
                }
            }
        });
    }

    final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper

    public UserAuth getToken(String code) throws AuthenticationException {

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("code", code);
        formData.add("redirect_uri", redirectUri);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", getAuthorizationHeader(clientId, clientSecret));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        Map<String, Object> map = postForMap(tokenEndpoint, formData, headers);

        if (map.containsKey("error")) {
            System.out.println("check_token returned error: " + map.get("error"));
            throw new AuthenticationException(code);
        }

        final UserAuth user = mapper.convertValue(map, UserAuth.class);

        return user;
    }

    public CheckToken getTokenIam(String token) throws AuthenticationException {

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("clientId", clientId);
        formData.add("clientSecret", clientSecret);
        formData.add("token", token);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", getAuthorizationHeader(clientId, clientSecret));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        Map<String, Object> map = postForMap(cekTokenEndpoint, formData, headers);

        if (map.containsKey("error")) {
            System.out.println("check_token returned error: " + map.get("error"));
            throw new AuthenticationException(token);
        }

        final CheckToken ct = mapper.convertValue(map, CheckToken.class);

        return ct;
    }

    private Map<String, Object> postForMap(String path, MultiValueMap<String, String> formData, HttpHeaders headers) {
        if (headers.getContentType() == null) {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        }
        @SuppressWarnings("rawtypes")
        Map map = restTemplate.exchange(path, HttpMethod.POST, new HttpEntity<>(formData, headers), Map.class)
                .getBody();
        @SuppressWarnings("unchecked")
        Map<String, Object> result = map;
        return result;
    }

    private String getAuthorizationHeader(String clientId, String clientSecret) {
        String creds = String.format("%s:%s", clientId, clientSecret);
        try {
            return "Basic " + new String(Base64.encode(creds.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Could not convert String");
        }
    }

    public List<String> getOtorisasi(List<GrantedAuthority> grantedAuthority) {
        List<String> authority = new ArrayList<>();
        for (GrantedAuthority a : grantedAuthority) {
            authority.add(a.getAuthority());
        }
        return authority;
    }
}
