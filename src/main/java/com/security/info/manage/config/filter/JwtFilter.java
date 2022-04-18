package com.security.info.manage.config.filter;

import com.security.info.manage.config.RequestHeaderContext;
import com.security.info.manage.dto.SimpleTokenInfo;
import com.security.info.manage.dto.res.UserResDTO;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.enums.TokenStatus;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/**
 * jwt过滤器
 *
 * @author frp
 */
@Slf4j
@Component
public class JwtFilter implements Filter {

    @Value("${excluded.pages}")
    private String[] pages;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (!Arrays.asList(pages).contains(httpRequest.getRequestURI()) || httpRequest.getRequestURI().contains("/static")) {
            chain.doFilter(httpRequest, httpResponse);
        } else {
            String token = httpRequest.getHeader("Authorization");
            if (token == null || StringUtils.isBlank(token)) {
                request.setAttribute("filter.error", new CommonException(ErrorCode.AUTHORIZATION_EMPTY));
                request.getRequestDispatcher("/error/exthrow").forward(request, response);
                return;
            }
            TokenStatus tokenStatus = TokenUtil.verifySimpleToken(token);
            switch (Objects.requireNonNull(tokenStatus)) {
                //有效
                case VALID:
                    UserResDTO userResDTO = TokenUtil.getSimpleTokenInfo(token);
                    new RequestHeaderContext.RequestHeaderContextBuild().user(userResDTO).build();
                    httpRequest.setAttribute("tokenInfo", userResDTO);
                    chain.doFilter(httpRequest, httpResponse);
                    break;
                //过期
                case EXPIRED:
                    request.setAttribute("filter.error", new CommonException(ErrorCode.AUTHORIZATION_IS_OVERDUE));
                    request.getRequestDispatcher("/error/exthrow").forward(request, response);
                    break;
                //无效
                default:
                    request.setAttribute("filter.error", new CommonException(ErrorCode.AUTHORIZATION_INVALID));
                    request.getRequestDispatcher("/error/exthrow").forward(request, response);
                    break;
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("jwtFilter init ...");
    }

    @Override
    public void destroy() {
        log.info("jwtFilter destroy ...");
    }
}