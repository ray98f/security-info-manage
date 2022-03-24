package com.security.info.manage.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.req.SpecialtyReqDTO;
import com.security.info.manage.dto.req.TransportReqDTO;
import com.security.info.manage.dto.res.PostResDTO;
import com.security.info.manage.dto.res.TransportResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface TransportMapper {

    Page<TransportResDTO> listTransport(Page<PostResDTO> page);

    List<TransportResDTO> listAllTransport();

    Integer selectTransportIsExist(TransportReqDTO transportReqDTO);

    Integer modifyTransport(TransportReqDTO transportReqDTO);

    Integer addTransport(TransportReqDTO transportReqDTO);

    void importTransport(List<TransportReqDTO> list);

    Integer deleteTransport(TransportReqDTO transportReqDTO);

}
