package com.ymc.iotthings.core.filter;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  拦截防止xss注入 通过Jsoup过滤请求参数内的特定字符
 *
 * package name: com.ymc.iotthings.core.filter
 * date :2019/5/29
 * author : ymc
 **/

public class XssFilter implements Filter  {
    //是否过滤富文本内容
    private static boolean IS_INCLUDE_RICH_TEXT = true;

    public List<String> excludes = new ArrayList<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String isIncludeRichText = filterConfig.getInitParameter("isIncludeRichText");
        if (StringUtils.isNotBlank(isIncludeRichText)) {
            IS_INCLUDE_RICH_TEXT = BooleanUtils.toBoolean(isIncludeRichText);
        }
        String temp = filterConfig.getInitParameter("excludes");
        if (temp != null) {
            String[] url = temp.split(",");
            for (int i = 0; url != null && i < url.length; i++) {
                excludes.add(url[i]);
            }
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        if (handleExcludeURL(req, resp)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        XssHttpServletRequestWrapper xssRequest = new
                XssHttpServletRequestWrapper((HttpServletRequest) servletRequest, IS_INCLUDE_RICH_TEXT);
        filterChain.doFilter(xssRequest, servletResponse);
    }

    private boolean handleExcludeURL(HttpServletRequest req, HttpServletResponse resp) {
        if (excludes == null || excludes.isEmpty()) {
            return false;
        }
        String url = req.getServletPath();
        for (String pattern : excludes) {
            Pattern p = Pattern.compile("^" + pattern);
            Matcher m = p.matcher(url);
            if (m.find()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {

    }
}
