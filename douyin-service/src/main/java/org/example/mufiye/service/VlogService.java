package org.example.mufiye.service;

import org.example.mufiye.bo.VlogBo;
import org.example.mufiye.utils.PagedGridResult;
import org.example.mufiye.vo.IndexVlogVo;

public interface VlogService {
    /**
     * 新增视频
     */
    public void createVlog(VlogBo vlogBo);

    /**
     * 查询视频
     */
    public PagedGridResult getIndexVlogList(String userId, String searchParam, Integer page, Integer pageSize);

    /**
     * 查询特定vlog id的视频
     */
    public IndexVlogVo getVlogDetailById(String userId, String vlogId);

    /**
     * 用户把视频改为公开或者私密的视频
     */
    public void changeToPrivateOrPublic(String userId, String vlogId, Integer yesOrNo);

    /**
     * 获取我的视频列表
     */
    public PagedGridResult queryMyVlogList(String userId, Integer page, Integer pageSize, Integer yesOrNo);

    /**
     * 用户点赞/喜欢视频
     */
    public void userLikeVlog(String userId, String vlogId);

    /**
     * 用户取消点赞/喜欢视频
     */
    public void userUnLikeVlog(String userId, String vlogId);

    /**
     * 用户获取其被点赞的视频信息
     */
    public Integer getVlogBeLikedCounts(String vlogId);

    /**
     * 用户获取其喜欢的视频列表
     */
    public PagedGridResult getMyLikedVlogList(String userId, Integer page, Integer pageSize);

    /**
     * 用户获取其关注博主的视频列表
     */
    public PagedGridResult getMyFollowVlogList(String myId, Integer page, Integer pageSize);

    /**
     * 用户获取其关注博主的视频列表
     */
    public PagedGridResult getMyFriendVlogList(String myId, Integer page, Integer pageSize);
}
