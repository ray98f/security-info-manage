package com.security.info.manage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.TransportReqDTO;
import com.security.info.manage.dto.res.TransportResDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author frp
 */
public interface TransportService {

    Page<TransportResDTO> listTransport(PageReqDTO pageReqDTO);

    List<TransportResDTO> listAllTransport();

    void modifyTransport(TransportReqDTO transportReqDTO);

    void addTransport(TransportReqDTO transportReqDTO);

    void importTransport(MultipartFile file);

    void deleteTransport(TransportReqDTO transportReqDTO);

}
