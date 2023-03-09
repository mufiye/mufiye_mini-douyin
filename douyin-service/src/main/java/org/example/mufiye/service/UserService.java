package org.example.mufiye.service;

import org.example.mufiye.bo.UpdatedDyUserBo;
import org.example.mufiye.pojo.DyUser;
import org.example.mufiye.vo.DyUserVo;

public interface UserService{
    /**
     * 判断用户是否存在
     */
    public DyUser queryMobileIsExist(String mobile);

    public DyUser createUser(String mobile);

    public DyUser getUser(String userId);

    public DyUser updateUserInfo(UpdatedDyUserBo updatedDyUserBo);

    public DyUser updateUserInfo(UpdatedDyUserBo updatedDyUserBo, Integer integer);
}
