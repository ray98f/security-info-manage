package com.security.info.manage.dto.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
public class SafeExpectInfoResDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "预想会id")
    private String safeExpectId;

    @ApiModelProperty(value = "天气")
    private String weather;

    @ApiModelProperty(value = "作业时温度")
    private String workTemperature;

    @ApiModelProperty(value = "风力")
    private String windPower;

    @ApiModelProperty(value = "相对湿度")
    private String relativeHumidity;

    @ApiModelProperty(value = "作业内容")
    private String workContent;

    @ApiModelProperty(value = "作业环境")
    private String workEnvironment;

    @ApiModelProperty(value = "具有安全风险的工器具")
    private String safeTools;

    @ApiModelProperty(value = "乘坐的交通运输方式")
    private String transportModes;

    @ApiModelProperty(value = "检修时的设备状态")
    private String overhaulEquipmentStatus;

    @ApiModelProperty(value = "风险描述-人的因素(作业风险类型)")
    private String describePersonFactor;

    @ApiModelProperty(value = "风险描述-物的因素(作业风险类型)")
    private String describeObjectFactor;

    @ApiModelProperty(value = "风险描述-环境因素(作业风险类型)")
    private String describeEnvironmentFactor;

    @ApiModelProperty(value = "风险描述-管理因素(作业风险类型)")
    private String describeManageFactor;

    @ApiModelProperty(value = "风险描述-本次作业可能发生(作业风险类型)")
    private String describeMayOccur;

    @ApiModelProperty(value = "风险描述-职业病危害因素(作业风险类型)")
    private String describeOccupationalHazards;

    @ApiModelProperty(value = "风险等级")
    private Integer riskLevel;

    @ApiModelProperty(value = "风险等级")
    private String riskLevelName;

    @ApiModelProperty(value = "风险等级")
    private String riskLevelColor;

    @ApiModelProperty(value = "风险管控措施")
    private String riskControlMeasures;

    @ApiModelProperty(value = "作业注意事项")
    private String workAttentionMatter;

    @ApiModelProperty(value = "安全技术交底")
    private String safeTechnicalDisclosure;

    @ApiModelProperty(value = "创建人")
    private String createBy;
}
