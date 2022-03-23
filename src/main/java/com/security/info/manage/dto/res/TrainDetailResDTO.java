package com.security.info.manage.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author frp
 */
@Data
public class TrainDetailResDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "培训id")
    private String trainId;

    @ApiModelProperty(value = "员工id")
    private String userNo;

    @ApiModelProperty(value = "员工姓名")
    private String userName;

    @ApiModelProperty(value = "岗位")
    private String postName;

    @ApiModelProperty(value = "部门")
    private String deptName;

    @ApiModelProperty(value = "教育时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date trainTime;

    @ApiModelProperty(value = "培训情况")
    private String trainCondition;

    @ApiModelProperty(value = "成绩")
    private String trainScore;

    @ApiModelProperty(value = "培训人名")
    private String createBy;
}
