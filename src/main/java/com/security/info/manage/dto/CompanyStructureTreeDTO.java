package com.security.info.manage.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
public class CompanyStructureTreeDTO {

    @ApiModelProperty(value = "组织编码")
    private String orgCode;

    @ApiModelProperty(value = "组织名称")
    private String orgName;

    @ApiModelProperty(value = "上级机构编码")
    private String parentOrgCode;

    @ApiModelProperty(value = "子集")
    private List<CompanyStructureTreeDTO> companyStructureTreeDTOList;
}
