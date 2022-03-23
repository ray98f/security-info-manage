package com.security.info.manage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.TrainReqDTO;
import com.security.info.manage.dto.res.TrainDetailResDTO;
import com.security.info.manage.dto.res.TrainResDTO;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author frp
 */
public interface TrainService {

    Page<TrainResDTO> listTrain(String startTime, String endTime, PageReqDTO pageReqDTO);

    TrainResDTO getTrainDetail(String id);

    Page<TrainDetailResDTO> listTrainUserDetail(String id, PageReqDTO pageReqDTO);

    void modifyTrain(TrainReqDTO trainReqDTO);

    void addTrain(TrainReqDTO trainReqDTO);

    void deleteTrain(TrainReqDTO trainReqDTO);

    void importTrainDetail(MultipartFile file, String trainId);

}
