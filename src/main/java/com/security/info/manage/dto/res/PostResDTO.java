package com.security.info.manage.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author frp
 */
@Data
public class PostResDTO {

    @ApiModelProperty(value = "组织id")
    private String id;

    @ApiModelProperty(value = "组织id")
    private String orgId;

    @ApiModelProperty(value = "组织名称")
    private String orgName;

    @ApiModelProperty(value = "上级机构id")
    private String postName;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date createDate;
}
