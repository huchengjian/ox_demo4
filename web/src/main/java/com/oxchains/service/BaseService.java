package com.oxchains.service;

import com.google.common.base.Charsets;
import com.oxchains.bean.dto.RespDTO;
import com.oxchains.handler.FluentResponseHandler;
import com.oxchains.service.dto.ChaincodeResultDTO;
import com.oxchains.util.ChaincodeJsonrpcUtils;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

/**
 * BaseService
 *
 * @author liuruichao
 * Created on 2017/3/11 11:41
 */
public abstract class BaseService {
    @Value("${chaincodeID}")
    protected String chaincodeID;

    @Value("${fabric.peer.address}")
    protected String fabricPeerAddress;

    protected RespDTO<String> sendChaincodeJsonrpcReq(String jsonrpc) throws IOException {
        String result = Request.Post(fabricPeerAddress + "/chaincode")
                .bodyByteArray(jsonrpc.getBytes(Charsets.UTF_8))
                .execute()
                .handleResponse(new FluentResponseHandler());
        ChaincodeResultDTO chaincodeResultDTO = ChaincodeResultDTO.parseObject(result);
        if (chaincodeResultDTO.getStatus() == 1) {
            return RespDTO.success(chaincodeResultDTO.getMessage(), chaincodeResultDTO.getData());
        }
        return RespDTO.fail(chaincodeResultDTO.getMessage() + "_" + chaincodeResultDTO.getData());
    }

    protected String genQuerySql(String dataItem, String id) {
        StringBuilder sbu = new StringBuilder();
        sbu.append("SELECT user_id, ").append(dataItem);
        sbu.append(" FROM medical_record where id=" + id);
        return sbu.toString();
    }
}
