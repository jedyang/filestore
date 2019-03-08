package com.yunsheng.filestore.service;

import com.yunsheng.filestore.entity.ApplyInfo;
import com.yunsheng.filestore.entity.Role;

import java.util.List;
import java.util.Set;

public interface ApplyService {
    int insertApply(ApplyInfo applyInfo);

    List<ApplyInfo> queryApplyInfo(String applyName);

    /**
     * 通过或拒绝申请
     * @param applyInfo
     * @return
     */
    long updateApply(ApplyInfo applyInfo);
}
