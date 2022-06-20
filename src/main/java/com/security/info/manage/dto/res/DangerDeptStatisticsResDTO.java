package com.security.info.manage.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class DangerDeptStatisticsResDTO {

    @ApiModelProperty(value = "部门id")
    private String deptId;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "部门统计数据")
    private DeptStatistics deptStatistics;

    @Data
    public static class DeptStatistics {

        @ApiModelProperty(value = "总整改问题数")
        private Integer total;

        @ApiModelProperty(value = "留存待整改问题数")
        private Integer legacy;

        @ApiModelProperty(value = "本月发现问题数")
        private Integer nowAdd;

        @ApiModelProperty(value = "本月整改问题数")
        private Integer nowSolve;

    }
}
