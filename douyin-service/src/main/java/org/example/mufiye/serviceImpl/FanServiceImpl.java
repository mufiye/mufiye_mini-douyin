package org.example.mufiye.serviceImpl;

import com.github.pagehelper.PageHelper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.example.mufiye.base.BaseInfoProperties;
import org.example.mufiye.enums.MessageEnum;
import org.example.mufiye.enums.YesOrNo;
import org.example.mufiye.mapper.FanMapper;
import org.example.mufiye.mapper.FanMapperCustom;
import org.example.mufiye.pojo.Fan;
import org.example.mufiye.service.FanService;
import org.example.mufiye.service.MsgService;
import org.example.mufiye.utils.PagedGridResult;
import org.example.mufiye.vo.FanVo;
import org.example.mufiye.vo.VlogerVo;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

@Service
public class FanServiceImpl extends BaseInfoProperties implements FanService {
    @Autowired
    private FanMapper fanMapper;

    @Autowired
    private FanMapperCustom fanMapperCustom;

    @Autowired
    private MsgService msgService;

    @Autowired
    private Sid sid;

    @Transactional
    @Override
    public void doFollow(final String userId, final String vlogerId) {
        String fid = sid.nextShort();
        Fan fan = new Fan();
        fan.setId(fid);
        fan.setFanId(userId);
        fan.setVlogerId(vlogerId);

        // 判断是否互相关注
        Fan vloger = queryFanRelationship(vlogerId, userId);
        if (vloger != null) {
            fan.setIsFanFriendOfMine(YesOrNo.YES.type);
            vloger.setIsFanFriendOfMine(YesOrNo.YES.type);
            fanMapper.updateByPrimaryKey(vloger);
        } else {
            fan.setIsFanFriendOfMine(YesOrNo.NO.type);
        }
        fanMapper.insert(fan);

        // 系统消息提示：关注
        msgService.createMsg(userId, vlogerId, MessageEnum.FOLLOW_YOU.type, null);
    }

    public Fan queryFanRelationship(String fanId, String vlogerId) {
        Example example = new Example(Fan.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("vlogerId", vlogerId);
        criteria.andEqualTo("fanId", fanId);

        List list = fanMapper.selectByExample(example);
        Fan fan = null;
        if (list != null && !list.isEmpty()) {
            fan = (Fan) list.get(0);
        }
        return fan;
    }

    @Transactional
    @Override
    public void doCancel(String userId, String vlogerId) {
        // 判断是否是朋友关系，如果是则需要取消
        Fan fan = queryFanRelationship(userId, vlogerId);
        if (fan != null && fan.getIsFanFriendOfMine() == YesOrNo.YES.type) {
            // 抹除双方的朋友关系，自己的关系删除即可
            Fan pendingFan = queryFanRelationship(vlogerId, userId);
            pendingFan.setIsFanFriendOfMine(YesOrNo.NO.type);
            fanMapper.updateByPrimaryKeySelective(pendingFan);
        }

        // 删除自己的关注关联表
        fanMapper.delete(fan);
    }

    @Override
    public boolean queryDoIFollowVloger(final String userId, final String vlogerId) {
        Fan vloger = queryFanRelationship(userId, vlogerId);
        return vloger != null;
    }

    @Override
    public PagedGridResult queryMyFollows(final String myId, final Integer page, final Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("myId", myId);

        PageHelper.startPage(page, pageSize);
        List<VlogerVo> list = fanMapperCustom.queryMyFollows(map);
        return setterPagedGrid(list, page);
    }

    @Override
    public PagedGridResult queryMyFans(final String myId, final Integer page, final Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("myId", myId);

        PageHelper.startPage(page, pageSize);
        List<FanVo> list = fanMapperCustom.queryMyFan(map);

        // 使前端产生互粉/回粉标记；如果我也关注了它，那么我们就是朋友关系（互粉关系）
        for (FanVo fanVo : list) {
            String relationship = redis.get(REDIS_FANS_AND_VLOGGER_RELATIONSHIP+":"+myId+":"+fanVo.getFanId());
            if (StringUtils.isNotBlank(relationship) && relationship.equalsIgnoreCase("1")) {
                fanVo.setFriend(true);
            }
        }

        return setterPagedGrid(list, page);
    }
}
