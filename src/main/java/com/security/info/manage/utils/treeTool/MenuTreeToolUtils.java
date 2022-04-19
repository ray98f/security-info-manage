package com.security.info.manage.utils.treeTool;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.security.info.manage.dto.res.LawCatalogResDTO;
import com.security.info.manage.dto.res.MenuResDTO;

import java.util.List;
import java.util.Map;

/**
 * @author frp
 */
public class MenuTreeToolUtils {
    /**
     * 根节点对象
     */
    private List<MenuResDTO> rootList;

    /**
     * 其他节点，可以包含根节点
     */
    private List<MenuResDTO> bodyList;

    public MenuTreeToolUtils(List<MenuResDTO> rootList, List<MenuResDTO> bodyList) {
        this.rootList = rootList;
        this.bodyList = bodyList;
    }

    public List<MenuResDTO> getTree() {
        if (bodyList != null && !bodyList.isEmpty()) {
            Map<String, String> map = Maps.newHashMapWithExpectedSize(bodyList.size());
            rootList.forEach(beanTree -> getChild(beanTree, map));
        }
        return rootList;
    }

    public void getChild(MenuResDTO menuResDTO, Map<String, String> map) {
        List<MenuResDTO> childList = Lists.newArrayList();
        bodyList.stream()
                .filter(c -> !map.containsKey(c.getId()))
                .filter(c -> c.getParentId().equals(menuResDTO.getId()))
                .forEach(c -> {
                    map.put(c.getId(), c.getParentId());
                    getChild(c, map);
                    childList.add(c);
                });
        menuResDTO.setChildren(childList);

    }
}
