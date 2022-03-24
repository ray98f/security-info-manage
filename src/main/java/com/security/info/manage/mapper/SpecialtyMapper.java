package com.security.info.manage.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.req.SpecialtyReqDTO;
import com.security.info.manage.dto.res.PostResDTO;
import com.security.info.manage.dto.res.SpecialtyResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface SpecialtyMapper {

    Page<SpecialtyResDTO> listSpecialty(Page<PostResDTO> page);

    List<SpecialtyResDTO> listAllSpecialty();

    Integer selectSpecialtyIsExist(SpecialtyReqDTO specialtyReqDTO);

    Integer modifySpecialty(SpecialtyReqDTO specialtyReqDTO);

    Integer addSpecialty(SpecialtyReqDTO specialtyReqDTO);

    void importSpecialty(List<SpecialtyReqDTO> list);

    Integer deleteSpecialty(SpecialtyReqDTO specialtyReqDTO);

}
