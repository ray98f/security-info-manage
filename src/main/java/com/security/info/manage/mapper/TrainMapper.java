package com.security.info.manage.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.req.TrainDetailReqDTO;
import com.security.info.manage.dto.req.TrainReqDTO;
import com.security.info.manage.dto.res.TrainDetailResDTO;
import com.security.info.manage.dto.res.TrainResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface TrainMapper {

    Page<TrainResDTO> listTrain(Page<TrainResDTO> page, String startTime, String endTime);

    TrainResDTO getTrainDetail(String id);

    Page<TrainDetailResDTO> listTrainUserDetail(Page<TrainDetailResDTO> page, String id);

    Integer modifyTrain(TrainReqDTO trainReqDTO);

    Integer addTrain(TrainReqDTO trainReqDTO);

    Integer deleteTrain(TrainReqDTO trainReqDTO);

    void importTrainDetail(List<TrainDetailReqDTO> list, String trainId);

    List<TrainDetailResDTO> userArchives(String id);

}
