package com.security.info.manage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author frp
 */
@ApiModel
@Data
public class BaseResDTO {

    @ApiModelProperty(value = "是否删除 0:否,1:是")
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createdAt;

    @ApiModelProperty(value = "创建人")
    private String createdBy;

    @ApiModelProperty(value = "修改时间")
    private Timestamp updatedAt;

    @ApiModelProperty(value = "修改人")
    private String updatedBy;

}
