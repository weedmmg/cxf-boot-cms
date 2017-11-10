package com.cxf.auth;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cxf.base.BaseController;
import com.cxf.entity.UserEntity;
import com.cxf.mapper.xrjf.UserMapper;
import com.cxf.secruity.JwtAuthenticationResponse;

//@RestController
//@RequestMapping("/auth")
public class AuthController extends BaseController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserMapper userMapper;

    @RequestMapping(value = "refresh", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) throws AuthenticationException {
        String token = request.getHeader(tokenHeader);
        String refreshedToken = authService.refresh(token);
        if (refreshedToken == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
        }
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public ModelAndView register(UserEntity addedUser) throws AuthenticationException {
        result.addObject("user", new UserEntity());

        result.setViewName("user/add");
        return result;
    }
}
