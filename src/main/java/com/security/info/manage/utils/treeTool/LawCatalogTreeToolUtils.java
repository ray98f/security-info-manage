package com.security.info.manage.utils.treeTool;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.security.info.manage.dto.res.DeptTreeResDTO;
import com.security.info.manage.dto.res.LawCatalogResDTO;

import java.util.List;
import java.util.Map;

/**
 * @author frp
 */
public class LawCatalogTreeToolUtils {
    /**
     * 根节点对象
     */
    private List<LawCatalogResDTO> rootList;

    /**
     * 其他节点，可以包含根节点
     */
    private List<LawCatalogResDTO> bodyList;

    public LawCatalogTreeToolUtils(List<LawCatalogResDTO> rootList, List<LawCatalogResDTO> bodyList) {
        this.rootList = rootList;
        this.bodyList = bodyList;
    }

    public List<LawCatalogResDTO> getTree() {
        if (bodyList != null && !bodyList.isEmpty()) {
            Map<String, String> map = Maps.newHashMapWithExpectedSize(bodyList.size());
            rootList.forEach(beanTree -> getChild(beanTree, map));
        }
        return rootList;
    }

    public void getChild(LawCatalogResDTO lawCatalogResDTO, Map<String, String> map) {
        List<LawCatalogResDTO> childList = Lists.newArrayList();
        bodyList.stream()
                .filter(c -> !map.containsKey(c.getId()))
                .filter(c -> c.getParentId().equals(lawCatalogResDTO.getId()))
                .forEach(c -> {
                    map.put(c.getId(), c.getParentId());
                    getChild(c, map);
                    childList.add(c);
                });
        lawCatalogResDTO.setChildren(childList);

    }
}
