package com.security.info.manage.service;

import com.security.info.manage.dto.req.VxSendMiniProgramMsgReqDTO;
import com.security.info.manage.dto.req.VxSendTextMsgReqDTO;

/**
 * @author frp
 */
public interface MsgService {

    void sendTextMsg(VxSendTextMsgReqDTO vxSendTextMsgReqDTO);

    void sendMiniProgramMsg(VxSendMiniProgramMsgReqDTO vxSendMiniProgramMsgReqDTO);

}
