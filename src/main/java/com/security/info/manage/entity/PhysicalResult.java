package com.security.info.manage.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author frp
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class PhysicalResult extends BaseEntity {

    @ApiModelProperty(value = "体检结果id")
    private String id;

    @ApiModelProperty(value = "体检人员id")
    private String physicalUserId;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "性别 0 未知 1 男 2 女")
    private Integer sex;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "接害工龄")
    private Integer workYear;

    @ApiModelProperty(value = "工种")
    private String workType;

    @ApiModelProperty(value = "接触职业病危害因素名称")
    private String hazardFactorName;

    @ApiModelProperty(value = "异常指标")
    private String warningIndex;

    @ApiModelProperty(value = "结论")
    private String conclusion;

    @ApiModelProperty(value = "处理意见")
    private String handlingOpinions;

    @ApiModelProperty(value = "医学建议")
    private String medicalAdvice;

    @ApiModelProperty(value = "结果")
    private Integer result;

    @ApiModelProperty(value = "是否已确认 0 否 1 是")
    private Integer isConfirm;
}
