package com.security.info.manage.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class EntryPlate {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "类型 1 建设问题 2 安全生产问题 3 安全隐患")
    private Integer type;

    @ApiModelProperty(value = "部门id")
    private String deptId;

    @ApiModelProperty(value = "部门id")
    private String deptName;

    @ApiModelProperty(value = "板块名称")
    private String plateName;

    @ApiModelProperty(value = "比例")
    private Double proportion;

    @Data
    public static class Entry {
        @ApiModelProperty(value = "词条id")
        private String id;

        @ApiModelProperty(value = "板块id")
        private String plateId;

        @ApiModelProperty(value = "词条内容")
        private String content;
    }

}
