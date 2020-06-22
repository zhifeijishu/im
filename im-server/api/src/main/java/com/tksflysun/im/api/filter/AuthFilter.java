package com.tksflysun.im.api.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.tksflysun.im.api.qo.UserQo;
import com.tksflysun.im.api.redis.UserInfoRedisCache;
import com.tksflysun.im.api.service.LoginLogService;
import com.tksflysun.im.api.service.UserService;
import com.tksflysun.im.api.util.SpringBeanUtils;
import com.tksflysun.im.api.util.UserLocalCache;
import com.tksflysun.im.common.bean.LoginLog;
import com.tksflysun.im.common.bean.User;
import com.tksflysun.im.common.exception.BusinessException;
import com.tksflysun.im.common.result.ControllerResult;

@Component
public class AuthFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        try {
            HttpServletRequest req = (HttpServletRequest)request;
            String token = req.getHeader("token");
            // TODO:拦截器的逻辑还需要进一步完善,用户信息redis缓存等
            if (req.getRequestURI().indexOf("/authentication") != -1
                || req.getRequestURI().indexOf("/registByMobile") != -1) {
            } else {
                if (StringUtils.isEmpty(token)) {
                    logger.error("================》登录token为空");
                    throw new BusinessException("用户未登录", ControllerResult.NO_LOGIN);
                }
                LoginLog loginLog =
                    ((LoginLogService)SpringBeanUtils.getBean("loginLogService")).getLoginLogByToken(token);
                if (loginLog == null) {
                    logger.error("登录token: " + token + "=============>找不到对应的登录日志记录");
                    HttpServletResponse res = (HttpServletResponse)response;
                    ControllerResult controllerResult = ControllerResult.getErrorResult();
                    controllerResult.setCode(ControllerResult.NO_LOGIN);
                    controllerResult.setMsg(ControllerResult.NO_LOGIN_MSG);
                    res.getOutputStream().write(JSON.toJSONString(controllerResult).getBytes());
                    res.getOutputStream().flush();
                    res.getOutputStream().close();
                    return;
                }
                User user = UserInfoRedisCache.getInstance().getT(loginLog.getUserId() + "", User.class);
                if (user == null) {
                    UserQo userQo = new UserQo();
                    userQo.setUserId(loginLog.getUserId());
                    user = ((UserService)SpringBeanUtils.getBean("userService")).getUser(userQo);
                    UserInfoRedisCache.getInstance().setT(loginLog.getUserId() + "", user);
                    UserInfoRedisCache.getInstance().expire(loginLog.getUserId() + "", 24 * 60 * 60);
                }
                UserLocalCache.setUser(user);
            }
            chain.doFilter(request, response);
        } finally {
            UserLocalCache.cleanUser();
        }
    }

    @Override
    public void destroy() {

    }

}
