package org.example.mufiye.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.mufiye.base.BaseInfoProperties;
import org.example.mufiye.exception.GraceException;
import org.example.mufiye.result.ResponseStatusEnum;
import org.example.mufiye.utils.IPUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class PassportInterceptor extends BaseInfoProperties implements HandlerInterceptor {
    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) throws Exception {
        String userIp = IPUtil.getRequestIp(request);
        if (redis.keyIsExist(MOBILE_SMSCODE + ":" + userIp)) {
            GraceException.display(ResponseStatusEnum.SMS_NEED_WAIT_ERROR);
            log.info("未到60s");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(final HttpServletRequest request,
                           final HttpServletResponse response,
                           final Object handler,
                           final ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(final HttpServletRequest request,
                                final HttpServletResponse response,
                                final Object handler,
                                final Exception ex) throws Exception {

    }
}
