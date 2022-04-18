package com.security.info.manage.utils.treeTool;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.security.info.manage.dto.res.LawCatalogResDTO;
import com.security.info.manage.dto.res.RegionResDTO;

import java.util.List;
import java.util.Map;

/**
 * @author frp
 */
public class RegionTreeToolUtils {
    /**
     * 根节点对象
     */
    private List<RegionResDTO> rootList;

    /**
     * 其他节点，可以包含根节点
     */
    private List<RegionResDTO> bodyList;

    public RegionTreeToolUtils(List<RegionResDTO> rootList, List<RegionResDTO> bodyList) {
        this.rootList = rootList;
        this.bodyList = bodyList;
    }

    public List<RegionResDTO> getTree() {
        if (bodyList != null && !bodyList.isEmpty()) {
            Map<String, String> map = Maps.newHashMapWithExpectedSize(bodyList.size());
            rootList.forEach(beanTree -> getChild(beanTree, map));
        }
        return rootList;
    }

    public void getChild(RegionResDTO regionResDTO, Map<String, String> map) {
        List<RegionResDTO> childList = Lists.newArrayList();
        bodyList.stream()
                .filter(c -> !map.containsKey(c.getId()))
                .filter(c -> c.getParentId().equals(regionResDTO.getId()))
                .forEach(c -> {
                    map.put(c.getId(), c.getParentId());
                    getChild(c, map);
                    childList.add(c);
                });
        regionResDTO.setChildren(childList);

    }
}
