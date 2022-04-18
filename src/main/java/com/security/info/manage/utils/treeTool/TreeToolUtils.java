package com.security.info.manage.utils.treeTool;

import com.security.info.manage.dto.res.DeptTreeResDTO;
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
    private List<DeptTreeResDTO> rootList;

    /**
     * 其他节点，可以包含根节点
     */
    private List<DeptTreeResDTO> bodyList;

    public TreeToolUtils(List<DeptTreeResDTO> rootList, List<DeptTreeResDTO> bodyList) {
        this.rootList = rootList;
        this.bodyList = bodyList;
    }

    public List<DeptTreeResDTO> getTree() {
        if (bodyList != null && !bodyList.isEmpty()) {
            //声明一个map，用来过滤已操作过的数据
            Map<String, String> map = Maps.newHashMapWithExpectedSize(bodyList.size());
            rootList.forEach(beanTree -> getChild(beanTree, map));
            return rootList;
        }
        return null;
    }

    public void getChild(DeptTreeResDTO deptTreeResDTO, Map<String, String> map) {
        List<DeptTreeResDTO> childList = Lists.newArrayList();
        bodyList.stream()
                .filter(c -> !map.containsKey(c.getOrgCode()))
                .filter(c -> c.getParentId().equals(deptTreeResDTO.getOrgCode()))
                .forEach(c -> {
                    map.put(c.getOrgCode(), c.getParentId());
                    getChild(c, map);
                    childList.add(c);
                });
        deptTreeResDTO.setChildren(childList);

    }
}
