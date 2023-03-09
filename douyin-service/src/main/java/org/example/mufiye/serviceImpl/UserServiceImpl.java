package org.example.mufiye.serviceImpl;

import java.util.Date;
import org.example.mufiye.bo.UpdatedDyUserBo;
import org.example.mufiye.enums.Sex;
import org.example.mufiye.enums.UserInfoModifyType;
import org.example.mufiye.enums.YesOrNo;
import org.example.mufiye.exception.GraceException;
import org.example.mufiye.mapper.DyUserMapper;
import org.example.mufiye.pojo.DyUser;
import org.example.mufiye.result.ResponseStatusEnum;
import org.example.mufiye.service.UserService;
import org.example.mufiye.utils.DateUtil;
import org.example.mufiye.utils.DesensitizationUtil;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private DyUserMapper dyUserMapper;

    @Autowired
    private Sid sid;

    private static final String USER_FACE1 = "https://raw.githubusercontent.com/mufiye/mufiye_backup/master/img/happy.jpg";

    @Override
    public DyUser queryMobileIsExist(final String mobile) {
        Example userExample = new Example(DyUser.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("mobile", mobile);
        DyUser dyUser = dyUserMapper.selectOneByExample(userExample);
        return dyUser;
    }

    @Transactional
    @Override
    public DyUser createUser(String mobile) {
        String userId = sid.nextShort();

        DyUser dyUser = new DyUser();
        dyUser.setId(userId);

        dyUser.setMobile(mobile);
        dyUser.setNickname("用户：" + DesensitizationUtil.commonDisplay(mobile));
        dyUser.setDouyinId("用户：" + DesensitizationUtil.commonDisplay(mobile));
        dyUser.setFace(USER_FACE1);

        dyUser.setBirthday(DateUtil.stringToDate("2000-05-14"));
        dyUser.setSex(Sex.secret.type);

        dyUser.setCountry("");
        dyUser.setProvince("");
        dyUser.setCity("");
        dyUser.setDistrict("");
        dyUser.setDescription("这家伙很懒，什么都没留下~");
        dyUser.setCanDouyinIdBeUpdated(YesOrNo.YES.type);

        dyUser.setCreatedTime(new Date());
        dyUser.setUpdatedTime(new Date());

        dyUserMapper.insert(dyUser);
        return dyUser;
    }

    @Override
    public DyUser getUser(final String userId) {
        return dyUserMapper.selectByPrimaryKey(userId);
    }

    @Transactional
    @Override
    public DyUser updateUserInfo(UpdatedDyUserBo updatedDyUserBo) {
        DyUser pendingUser = new DyUser();
        BeanUtils.copyProperties(updatedDyUserBo, pendingUser);

        int result = dyUserMapper.updateByPrimaryKeySelective(pendingUser);
        if(result != 1) {
            GraceException.display(ResponseStatusEnum.USER_INFO_UPDATED_ERROR);
        }
        return getUser(updatedDyUserBo.getId());
    }

    @Transactional
    @Override
    public DyUser updateUserInfo(UpdatedDyUserBo updatedDyUserBo, Integer type) {
        Example example = new Example(DyUser.class);
        Example.Criteria criteria = example.createCriteria();

        if (type == UserInfoModifyType.NICKNAME.type) {  // nickname不能重复
            criteria.andEqualTo("nickname", updatedDyUserBo.getNickname());
            DyUser dyUser = dyUserMapper.selectOneByExample(example);
            if (dyUser != null) {
                GraceException.display(ResponseStatusEnum.USER_INFO_UPDATED_ERROR);
            }
        }
        if (type == UserInfoModifyType.DouyinId.type) {  // 抖音id要求可以更改
            criteria.andEqualTo("douyin_id", updatedDyUserBo.getDouyinId());
            DyUser dyUser = dyUserMapper.selectOneByExample(example);
            if (dyUser != null) {
                GraceException.display(ResponseStatusEnum.USER_INFO_UPDATED_ERROR);
            }
            DyUser tempUser = getUser(updatedDyUserBo.getId());
            if(tempUser.getCanDouyinIdBeUpdated() == YesOrNo.NO.type) {
                GraceException.display(ResponseStatusEnum.USER_INFO_CANT_UPDATED_DOUYINID_ERROR);
            }
            updatedDyUserBo.setCanDouyinIdBeUpdated(YesOrNo.NO.type);
        }

        return updateUserInfo(updatedDyUserBo);
    }
}
