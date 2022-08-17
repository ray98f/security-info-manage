package com.security.info.manage.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.security.info.manage.entity.File;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author frp
 */
@Data
public class LawResDTO {

    @ApiModelProperty(value = "法律法规文件id")
    private String id;

    @ApiModelProperty(value = "法律法规目录id")
    private String lawCatalogId;

    @ApiModelProperty(value = "法律法规目录名称")
    private String lawCatalogName;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "文件信息")
    private File fileInfo;

    @ApiModelProperty(value = "分区")
    private String bizCode;

    @ApiModelProperty(value = "文件路径")
    private String fileUrl;

    @ApiModelProperty(value = "创建人员id")
    private String createBy;

    @ApiModelProperty(value = "创建人员名称")
    private String createName;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date createDate;

    @ApiModelProperty(value = "修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date updateDate;

    @ApiModelProperty(value = "排序")
    private Integer sort;


}
