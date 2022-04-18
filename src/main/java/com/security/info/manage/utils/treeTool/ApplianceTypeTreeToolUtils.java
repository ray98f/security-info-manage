package com.security.info.manage.utils.treeTool;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.security.info.manage.dto.res.ApplianceTypeTreeResDTO;
import com.security.info.manage.dto.res.DeptTreeResDTO;

import java.util.List;
import java.util.Map;

/**
 * @author frp
 */
public class ApplianceTypeTreeToolUtils {
    /**
     * 根节点对象
     */
    private List<ApplianceTypeTreeResDTO> rootList;

    /**
     * 其他节点，可以包含根节点
     */
    private List<ApplianceTypeTreeResDTO> bodyList;

    public ApplianceTypeTreeToolUtils(List<ApplianceTypeTreeResDTO> rootList, List<ApplianceTypeTreeResDTO> bodyList) {
        this.rootList = rootList;
        this.bodyList = bodyList;
    }

    public List<ApplianceTypeTreeResDTO> getTree() {
        if (bodyList != null && !bodyList.isEmpty()) {
            //声明一个map，用来过滤已操作过的数据
            Map<String, String> map = Maps.newHashMapWithExpectedSize(bodyList.size());
            rootList.forEach(beanTree -> getChild(beanTree, map));
        }
        return rootList;
    }

    public void getChild(ApplianceTypeTreeResDTO applianceTypeTreeResDTO, Map<String, String> map) {
        List<ApplianceTypeTreeResDTO> childList = Lists.newArrayList();
        bodyList.stream()
                .filter(c -> !map.containsKey(c.getId()))
                .filter(c -> c.getParentId().equals(applianceTypeTreeResDTO.getId()))
                .forEach(c -> {
                    map.put(c.getId(), c.getParentId());
                    getChild(c, map);
                    childList.add(c);
                });
        applianceTypeTreeResDTO.setChildren(childList);
    }
}
