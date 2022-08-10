package id.go.pajak.web.controller;

import id.go.pajak.web.model.CheckToken;
import id.go.pajak.web.model.UserAuth;
import id.go.pajak.web.service.AuthService;
import javax.naming.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

    @Autowired
    AuthService authService;

    @GetMapping(value = "/")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        if (session.getAttribute("auth") != null) {
            return new ModelAndView(new RedirectView("/index", true, false));
        } else {
            return new ModelAndView(new RedirectView("/auth/login", false, false));
        }
    }

    @GetMapping("/index")
    public ModelAndView dashboard(HttpServletRequest request) {
        HttpSession session = request.getSession();

        return new ModelAndView("index");

    }

   @GetMapping("/userauth")
    public Object getUserAuth(HttpServletRequest request) throws AuthenticationException {
        HttpSession session = request.getSession();
        UserAuth userAuth = (UserAuth) session.getAttribute("auth");

        if (session.getAttribute("auth") == null) {
            return "SESSION TIDAK ADA";

        } else {
            return userAuth;
        }

    }

    @GetMapping("/checktoken")
    public Object getCheckToken(HttpServletRequest request) throws AuthenticationException {
        HttpSession session = request.getSession();
        UserAuth userAuth = (UserAuth) session.getAttribute("auth");

        if (session.getAttribute("auth") == null) {
            return "SESSION TIDAK ADA";

        } else {
            return authService.getTokenIam(userAuth.getAccess_token());
        }

    }
}
