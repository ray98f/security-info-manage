package com.security.info.manage.dto.req;

import com.security.info.manage.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author frp
 */
@Data
public class PhysicalNoticeReqDTO {

    @ApiModelProperty(value = "体检流程id")
    private String physicalId;

    @ApiModelProperty(value = "选择人员")
    private List<User> users;

    @Data
    public static class PhysicalNoticeUser {

        @ApiModelProperty(value = "用户体检id")
        private String physicalUserId;

        @ApiModelProperty(value = "用户id")
        private String userId;
    }

}
