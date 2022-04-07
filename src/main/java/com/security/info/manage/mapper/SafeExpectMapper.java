package com.security.info.manage.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.req.SafeExpectCollectionUnionReqDTO;
import com.security.info.manage.dto.req.SafeExpectInfoReqDTO;
import com.security.info.manage.dto.req.SafeExpectReqDTO;
import com.security.info.manage.dto.res.SafeExpectCollectionUnionResDTO;
import com.security.info.manage.dto.res.SafeExpectInfoResDTO;
import com.security.info.manage.dto.res.SafeExpectResDTO;
import com.security.info.manage.dto.res.SafeExpectUserResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface SafeExpectMapper {

    Page<SafeExpectResDTO> listSafeExpect(Page<SafeExpectResDTO> page);

    SafeExpectResDTO getSafeExpectDetail(String id);

    SafeExpectInfoResDTO getSafeExpectInfoDetail(String id);

    SafeExpectCollectionUnionResDTO getSafeExpectCollectionUnionDetail(String id);

    Integer selectSafeExpectIsExist(SafeExpectReqDTO safeExpectReqDTO);

    Integer addSafeExpect(SafeExpectReqDTO safeExpectReqDTO);

    Integer insertSafeExpectUser(String id, List<String> list);

    Integer modifySafeExpectInfo(SafeExpectInfoReqDTO safeExpectInfoReqDTO);

    Integer modifySafeExpectCollectionUnion(SafeExpectCollectionUnionReqDTO safeExpectCollectionUnionReqDTO);

    Integer deleteSafeExpect(SafeExpectReqDTO safeExpectReqDTO);

    Page<SafeExpectUserResDTO> listSafeExpectUser(Page<SafeExpectUserResDTO> page, String id);

    Integer signSafeExpectUser(SafeExpectUserResDTO safeExpectUserResDTO);

}
