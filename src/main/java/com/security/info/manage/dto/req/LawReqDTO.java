package com.security.info.manage.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
public class LawReqDTO {

    @ApiModelProperty(value = "法律法规id")
    private String id;

    @ApiModelProperty(value = "法律法规目录id")
    private String lawCatalogId;

    @ApiModelProperty(value = "文件id")
    private String fileId;

    @ApiModelProperty(value = "文件链接")
    private String fileUrl;

    @ApiModelProperty(value = "文件ids")
    private List<String> fileIds;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "创建人员id")
    private String createBy;


}
