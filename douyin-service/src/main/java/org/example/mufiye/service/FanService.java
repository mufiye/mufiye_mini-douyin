package org.example.mufiye.service;

import org.example.mufiye.utils.PagedGridResult;

public interface FanService {
    // 关注
    public void doFollow(String userId, String vlogerId);

    // 取消关注
    public void doCancel(String userId, String vlogerId);

    // 查询是否关注
    public boolean queryDoIFollowVloger(String userId, String vlogerId);

    // 查询我关注的博主列表
    public PagedGridResult queryMyFollows(String myId, Integer page, Integer pageSize);

    // 查询我的粉丝列表
    public PagedGridResult queryMyFans(String myId, Integer page, Integer pageSize);
}
