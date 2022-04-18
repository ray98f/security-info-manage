package com.security.info.manage.config;

import com.security.info.manage.dto.SimpleTokenInfo;
import com.security.info.manage.dto.res.UserResDTO;
import lombok.Data;


/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2020/4/30 15:00
 */
@Data
public class RequestHeaderContext {

    private static final ThreadLocal<RequestHeaderContext> REQUEST_HEADER_CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

    private UserResDTO user;

    public static RequestHeaderContext getInstance() {
        return REQUEST_HEADER_CONTEXT_THREAD_LOCAL.get();
    }

    public void setContext(RequestHeaderContext context) {
        REQUEST_HEADER_CONTEXT_THREAD_LOCAL.set(context);
    }

    public static void clean() {
        REQUEST_HEADER_CONTEXT_THREAD_LOCAL.remove();
    }

    private RequestHeaderContext(RequestHeaderContextBuild requestHeaderContextBuild) {
        this.user = requestHeaderContextBuild.user;
        setContext(this);
    }

    @Data
    public static class RequestHeaderContextBuild {

        private UserResDTO user;

        public RequestHeaderContextBuild user(UserResDTO user) {
            this.user = user;
            return this;
        }

        public RequestHeaderContext build() {
            return new RequestHeaderContext(this);
        }
    }

}
