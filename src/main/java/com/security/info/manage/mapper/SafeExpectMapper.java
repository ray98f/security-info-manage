package com.security.info.manage.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.req.SafeExpectCollectionUnionReqDTO;
import com.security.info.manage.dto.req.SafeExpectInfoReqDTO;
import com.security.info.manage.dto.req.SafeExpectReqDTO;
import com.security.info.manage.dto.res.SafeExpectCollectionUnionResDTO;
import com.security.info.manage.dto.res.SafeExpectInfoResDTO;
import com.security.info.manage.dto.res.SafeExpectResDTO;
import com.security.info.manage.dto.res.SafeExpectUserResDTO;
import com.security.info.manage.entity.SafeExpectUser;
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

    Page<SafeExpectResDTO> vxListSafeExpect(Page<SafeExpectResDTO> page, String userId);

    SafeExpectResDTO getSafeExpectDetail(String id);

    SafeExpectInfoResDTO getSafeExpectInfoDetail(String id);

    SafeExpectInfoResDTO exportSafeExpectInfo(String id);

    SafeExpectCollectionUnionResDTO getSafeExpectCollectionUnionDetail(String id);

    List<SafeExpectUser> getSafeExpectUserInfo(String id);

    Integer selectSafeExpectIsExist(SafeExpectReqDTO safeExpectReqDTO);

    Integer addSafeExpect(SafeExpectReqDTO safeExpectReqDTO);

    Integer insertSafeExpectUser(String id, List<String> list);

    Integer modifySafeExpect(SafeExpectReqDTO safeExpectReqDTO);

    Integer modifySafeExpectInfo(SafeExpectInfoReqDTO safeExpectInfoReqDTO);

    Integer modifySafeExpectCollectionUnion(SafeExpectCollectionUnionReqDTO safeExpectCollectionUnionReqDTO);

    Integer deleteSafeExpect(SafeExpectReqDTO safeExpectReqDTO);

    List<String> selectUserIdById(String id);

    Integer cancelSafeExpect(SafeExpectReqDTO safeExpectReqDTO);

    Page<SafeExpectUserResDTO> listSafeExpectUser(Page<SafeExpectUserResDTO> page, String id);

    Integer signSafeExpectUser(SafeExpectUserResDTO safeExpectUserResDTO);

    Integer vxSignSafeExpectUser(String id, String userId);

    void modifySafeExpectStatus();

}
