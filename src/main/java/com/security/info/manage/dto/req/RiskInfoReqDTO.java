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
public class RiskInfoReqDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "风险项类型 1 运营类 2 生产类")
    private Integer type;

    @ApiModelProperty(value = "所属专业模块")
    private String modules;

    @ApiModelProperty(value = "主要风险点")
    private String riskPoints;

    @ApiModelProperty(value = "主风险点分项")
    private String mainRiskPoints;

    @ApiModelProperty(value = "风险描述")
    private String describeText;

    @ApiModelProperty(value = "风险描述-危险源及其可能造成的后果(行车风险类型)")
    private String describeResult;

    @ApiModelProperty(value = "事故类型")
    private String accidentType;

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

    @ApiModelProperty(value = "风险矩阵法-L(作业风险类型)")
    private String riskMatrixL;

    @ApiModelProperty(value = "风险矩阵法-S(作业风险类型)")
    private String riskMatrixS;

    @ApiModelProperty(value = "风险矩阵法-R(作业风险类型)")
    private String riskMatrixR;

    @ApiModelProperty(value = "风险定量评价-L(行车风险类型)")
    private String riskEvaluationL;

    @ApiModelProperty(value = "风险定量评价-C(行车风险类型)")
    private String riskEvaluationC;

    @ApiModelProperty(value = "风险定量评价-D(行车风险类型)")
    private String riskEvaluationD;

    @ApiModelProperty(value = "风险等级 1 重大风险 2 较大风险 3 一般风险 4 较小风险")
    private Integer level;

    @ApiModelProperty(value = "技术措施")
    private String technicalMeasures;

    @ApiModelProperty(value = "管理措施")
    private String manageMeasures;

    @ApiModelProperty(value = "教育措施")
    private String educationMeasures;

    @ApiModelProperty(value = "个体防护")
    private String individualProtection;

    @ApiModelProperty(value = "应急措施")
    private String emergencyMeasure;

    @ApiModelProperty(value = "职业健康防护措施(作业风险类型)")
    private String healthProtectMeasures;

    @ApiModelProperty(value = "措施指定依据(关联法律、法规、规章、制度文档)")
    private String basis;

    @ApiModelProperty(value = "责任部门")
    private String responsibilityDept;

    @ApiModelProperty(value = "责任中心")
    private String responsibilityCenter;

    @ApiModelProperty(value = "责任岗位")
    private String responsibilityPost;

    @ApiModelProperty(value = "责任人")
    private String responsibilityUser;

    @ApiModelProperty(value = "创建人")
    private String createBy;
}
