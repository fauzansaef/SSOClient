package id.go.pajak.web.controller;

import id.go.pajak.web.config.CustomRemoteTokenService;
import id.go.pajak.web.model.CheckToken;
import id.go.pajak.web.model.UserAuth;
import id.go.pajak.web.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@Controller
@RequestMapping(value = "/auth")
public class AuthController {

    @Value("${app.role}")
    private String role;

    @Value("${auth.sso}")
    private String ssoWeb;
    @Value("${auth.account}")
    private String accountWeb;

    // production
    @Value("${auth.ClientId}")
    private String clientId;
    @Value("${auth.RedirectUri}")
    private String redirectUri;
    @Value("${auth.AuthEndpoint}")
    private String authorizationEndpoint;
    @Value("${auth.LogoutUri}")
    private String logoutUri;
    @Value("${auth.Apphost}")
    private String apphost;

    @Autowired
    private AuthService authService;

    @Autowired
    private CustomRemoteTokenService tokenService;


    @GetMapping(value = "/login")
    public ModelAndView login() {
        return new ModelAndView(new RedirectView(authorizationEndpoint + "?client_id=" + clientId
                + "&response_type=code&redirect_uri=" + redirectUri, true, true));
    }

    @RequestMapping(value = "/cek", method = RequestMethod.GET)
    public ModelAndView cek(@RequestParam(required = false) String code, RedirectAttributes redirectAttributes,
                            HttpSession sessionObj, HttpServletResponse response) throws IOException, AuthenticationException {
        if (!code.isEmpty()) {
            // tukarkan code dengan access_token
            // bawa access_token untuk akses halaman yang dipagari dengan oauth
            UserAuth user = authService.getToken(code);
            if (user != null) {
                sessionObj.isNew();
                sessionObj.setAttribute("auth", user);
                if (null != user.getAccess_token()) {
                    CheckToken map = authService.getTokenIam(user.getAccess_token());
                    System.out.println(map);
                    sessionObj.setAttribute("checktoken", map);
                    return new ModelAndView(new RedirectView("/index", true, false));
                } else {
                    return new ModelAndView(new RedirectView("/auth/login", true, false));
                }
            } else {
                return new ModelAndView(new RedirectView("/auth/login", true, false));
            }
        } else {
            return new ModelAndView(new RedirectView("/auth/login", true, false));
        }
    }

    @GetMapping(value = "/logout")
    public ModelAndView logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("auth") != null) {
            UserAuth user = (UserAuth) session.getAttribute("auth");
            session.invalidate();
            if (null != user) {
                return new ModelAndView(new RedirectView(
                        logoutUri + "?token=" + user.getAccess_token() + "&redirect=" + apphost, true, false));
            } else {
                return new ModelAndView(new RedirectView("/auth/login", true, false));
            }
        } else {
            return new ModelAndView(new RedirectView("/auth/login", true, false));
        }
    }

    @GetMapping(value = "/profile")
    public ModelAndView profil() {
        return new ModelAndView(new RedirectView(accountWeb + "/profil", true, false));
    }

    public String checkAuth(HttpServletRequest request) {
        String urlRedirect  = "/auth/login";;
        HttpSession session = request.getSession();
        if (session.getAttribute("auth") != null) {
            UserAuth authModel = (UserAuth) session.getAttribute("auth");
            OAuth2Authentication authentication = null;
            try {
                authentication = tokenService.loadAuthentication(authModel.getAccess_token());
            } catch (InvalidTokenException e) {
                urlRedirect = "/auth/login";
                return urlRedirect;
            }

        } else {
            urlRedirect = "/auth/login";
        }

        return urlRedirect;
    }
}
