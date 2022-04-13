package com.security.info.manage.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author frp
 */
@Data
public class DangerReqDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "编号")
    private String no;

    @ApiModelProperty(value = "隐患区域")
    private String regionId;

    @ApiModelProperty(value = "隐患地点")
    private String address;

    @ApiModelProperty(value = "隐患问题内容")
    private String content;

    @ApiModelProperty(value = "整改前图片")
    private String beforePic;

    @ApiModelProperty(value = "整改期限")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(
            pattern = "yyyy-MM-dd",
            timezone = "GMT+8"
    )
    private Date rectifyTerm;

    @ApiModelProperty(value = "检查部门id")
    private String checkDeptId;

    @ApiModelProperty(value = "检查人id")
    private String checkUserId;

    @ApiModelProperty(value = "检查时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date checkTime;

    @ApiModelProperty(value = "是否为标准化建设问题 0 否 1 是")
    private Integer isBuildProb;

    @ApiModelProperty(value = "建设问题词条id")
    private String buildEntryId;

    @ApiModelProperty(value = "建设问题扣除分值")
    private Double buildScore;

    @ApiModelProperty(value = "换算分值")
    private Double buildConversionScore;

    @ApiModelProperty(value = "是否为安全生产问题 0 否 1 是")
    private Integer isProdProb;

    @ApiModelProperty(value = "安全生产问题词条id")
    private String prodEntryId;

    @ApiModelProperty(value = "安全生产问题类别")
    private Integer prodCategory;

    @ApiModelProperty(value = "安全生产问题考核分值")
    private Double prodEntryScore;

    @ApiModelProperty(value = "是否为安全隐患问题 0 否 1 是")
    private Integer isDangerProb;

    @ApiModelProperty(value = "安全隐患问题隐患等级")
    private Integer level;

    @ApiModelProperty(value = "安全隐患问题隐患类别")
    private Integer dangerCategory;

    @ApiModelProperty(value = "是否广播隐患信息 0 否 1 是")
    private Integer isPush;

    @ApiModelProperty(value = "是否使用 0 否 1 是")
    private Integer isUse;

    @ApiModelProperty(value = "状态 0 未审核 1 审核中 2 审核通过待下发 3 已下发待整改 4 整改中 5 整改完成 6 审核不通过")
    private Integer status;

    @ApiModelProperty(value = "用户id")
    private String createBy;

    @ApiModelProperty(value = "审核人id")
    private String examineUserId;

}
