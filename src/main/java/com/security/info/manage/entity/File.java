package com.security.info.manage.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author frp
 */
@Data
@ApiModel
public class File {

    @ApiModelProperty(value = "文件id")
    private String id;

    @ApiModelProperty(value = "文件名")
    private String fileName;

    @ApiModelProperty(value = "标识字符")
    private String bizCode;

    @ApiModelProperty(value = "链接")
    private String fileUrl;
}
