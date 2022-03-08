package com.security.info.manage.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author frp
 */
@Data
public class DeptTreeResDTO {

    @ApiModelProperty(value = "组织id")
    private String id;

    @ApiModelProperty(value = "组织名称")
    private String orgName;

    @ApiModelProperty(value = "组织编号")
    private String orgCode;

    @ApiModelProperty(value = "上级机构id")
    private String parentId;

    @ApiModelProperty(value = "序号")
    private Integer sort;

    @ApiModelProperty(value = "部门负责人s")
    private String userIds;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date createDate;

    @ApiModelProperty(value = "子集")
    private List<DeptTreeResDTO> children;
}
