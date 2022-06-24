package com.security.info.manage.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.security.info.manage.entity.File;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author frp
 */
@Data
public class DangerExportResDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "编号")
    private String no;

    @ApiModelProperty(value = "隐患区域名称")
    private String regionName;

    @ApiModelProperty(value = "隐患地点")
    private String address;

    @ApiModelProperty(value = "隐患问题内容")
    private String content;

    @ApiModelProperty(value = "整改前图片")
    private String beforePic;

    @ApiModelProperty(value = "整改期限")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date rectifyTerm;

    @ApiModelProperty(value = "检查部门名称")
    private String checkDeptName;

    @ApiModelProperty(value = "检查人名称")
    private String checkUserName;

    @ApiModelProperty(value = "检查时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date checkTime;

    @ApiModelProperty(value = "建设问题词条板块名称")
    private String buildPlateName;

    @ApiModelProperty(value = "建设问题词条内容")
    private String buildEntry;

    @ApiModelProperty(value = "建设问题扣除分值")
    private Double buildScore;

    @ApiModelProperty(value = "换算分值")
    private Double buildConversionScore;

    @ApiModelProperty(value = "安全生产问题词条板块名称")
    private String prodPlateName;

    @ApiModelProperty(value = "安全生产问题词条内容")
    private String prodEntry;

    @ApiModelProperty(value = "安全生产问题类别")
    private String prodCategory;

    @ApiModelProperty(value = "安全生产问题考核分值")
    private Double prodEntryScore;

    @ApiModelProperty(value = "安全隐患问题隐患等级")
    private Integer level;

    @ApiModelProperty(value = "安全隐患问题隐患类别")
    private Integer dangerCategory;

    @ApiModelProperty(value = "是否消项 0 否 1 是")
    private Integer isEliminate;

    private String afterPic;

    private String responsibilityDeptName;

    private String rectifyUserName;

    private String responsibilityDeptId;

    private String rectifyMeasure;

}
