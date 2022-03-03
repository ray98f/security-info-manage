package com.security.info.manage.utils;

import com.security.info.manage.dto.CompanyStructureTreeDTO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * @author frp
 */
public class TreeToolUtils {
    /**
     * 根节点对象
     */
    private List<CompanyStructureTreeDTO> rootList;

    /**
     * 其他节点，可以包含根节点
     */
    private List<CompanyStructureTreeDTO> bodyList;

    public TreeToolUtils(List<CompanyStructureTreeDTO> rootList, List<CompanyStructureTreeDTO> bodyList) {
        this.rootList = rootList;
        this.bodyList = bodyList;
    }

    public List<CompanyStructureTreeDTO> getTree() {
        if (bodyList != null && !bodyList.isEmpty()) {
            //声明一个map，用来过滤已操作过的数据
            Map<String, String> map = Maps.newHashMapWithExpectedSize(bodyList.size());
            rootList.forEach(beanTree -> getChild(beanTree, map));
            return rootList;
        }
        return null;
    }

    public void getChild(CompanyStructureTreeDTO companyStructureTreeDTO, Map<String, String> map) {
        List<CompanyStructureTreeDTO> childList = Lists.newArrayList();
        bodyList.stream()
                .filter(c -> !map.containsKey(c.getOrgCode()))
                .filter(c ->c.getParentOrgCode().equals(companyStructureTreeDTO.getOrgCode()))
                .forEach(c ->{
                    map.put(c.getOrgCode(),c.getParentOrgCode());
                    getChild(c,map);
                    childList.add(c);
                });
        companyStructureTreeDTO.setCompanyStructureTreeDTOList(childList);

    }
}
