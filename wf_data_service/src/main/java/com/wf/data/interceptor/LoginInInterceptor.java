package com.wf.data.interceptor;

import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.IPUtils;
import com.wf.core.utils.TraceIdUtils;
import com.wf.data.dao.datarepo.entity.DataLoginInLog;
import com.wf.data.service.data.DataLoginInLogService;
import org.jasig.cas.client.util.AssertionHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author: lcs
 * @date: 2018/06/26
 */
public class LoginInInterceptor implements HandlerInterceptor {
    private static Logger logger = LoggerFactory.getLogger(LoginInInterceptor.class);
    @Autowired
    private DataLoginInLogService dataLoginInLogService;

    /**
     * 该方法将在请求处理之前进行调用，
     * 只有该方法返回true，才会继续执行后续的Interceptor和Controller，
     * 当返回值为true 时就会继续调用下一个Interceptor的preHandle 方法，
     * 如果已经是最后一个Interceptor的时候就会是调用当前请求的Controller方法；
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long startTime = System.currentTimeMillis();
        request.setAttribute("login_time", startTime);
        return true;
    }

    /**
     * 该方法将在请求处理之后，
     * DispatcherServlet进行视图返回渲染之前进行调用，
     * 可以在这个方法中对Controller 处理之后的ModelAndView
     * 对象进行操作。
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 该方法也是需要当前对应的Interceptor的preHandle方法的返回值为true时才会执行，
     * 该方法将在整个请求结束之后，
     * 也就是在DispatcherServlet 渲染了对应的视图之后执行。
     * 用于进行资源清理。
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        try {
            HandlerMethod h = (HandlerMethod) handler;
            String requestClass = h.getBean().getClass().getName();
            List<String> ignoreUrlList = Arrays.asList("com.wf.data.controller.admin.CommonDataController", "com.wf.data.controller.admin.HomeController","com.wf.data.controller.admin.DataDictController");
            if (ignoreUrlList.contains(requestClass)) {
                return;
            }
            Long startTime = (Long) request.getAttribute("login_time");
            Long endTime = System.currentTimeMillis();
            Long executeTime = endTime - startTime;

            String loginName = AssertionHolder.getAssertion().getPrincipal().getName();

            DataLoginInLog log = new DataLoginInLog();
            log.setLoginName(loginName);
            log.setExecuteTime(executeTime.intValue());


            log.setRequestClass(requestClass);
            log.setRequestMethod(h.getMethod().getName());
            log.setRequestUrl(request.getRequestURI());
            log.setRequestIp(IPUtils.getRemoteAddress(request));
            log.setCreateTime(new Date());
            dataLoginInLogService.save(log);
        } catch (Exception e) {
            logger.error("LoginInInterceptor 处理错误: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
        }

    }
}
