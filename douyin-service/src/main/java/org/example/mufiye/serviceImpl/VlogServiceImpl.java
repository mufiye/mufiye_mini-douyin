package org.example.mufiye.serviceImpl;

import com.github.pagehelper.PageHelper;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.example.mufiye.base.BaseInfoProperties;
import org.example.mufiye.bo.VlogBo;
import org.example.mufiye.enums.MessageEnum;
import org.example.mufiye.enums.YesOrNo;
import org.example.mufiye.mapper.FanMapper;
import org.example.mufiye.mapper.MyLikedVlogMapper;
import org.example.mufiye.mapper.VlogMapper;
import org.example.mufiye.mapper.VlogMapperCustom;
import org.example.mufiye.pojo.MyLikedVlog;
import org.example.mufiye.pojo.Vlog;
import org.example.mufiye.service.FanService;
import org.example.mufiye.service.MsgService;
import org.example.mufiye.service.VlogService;
import org.example.mufiye.utils.PagedGridResult;
import org.example.mufiye.vo.IndexVlogVo;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

@Service
public class VlogServiceImpl extends BaseInfoProperties implements VlogService {
    @Autowired
    private VlogMapper vlogMapper;

    @Autowired
    private VlogMapperCustom vlogMapperCustom;

    @Autowired
    private MyLikedVlogMapper myLikedVlogMapper;

    @Autowired
    private FanService fanService;

    @Autowired
    private MsgService msgService;

    @Autowired
    private Sid sid;

    @Transactional
    @Override
    public void createVlog(VlogBo vlogBo) {
        String vid = sid.nextShort();

        Vlog vlog = new Vlog();
        BeanUtils.copyProperties(vlogBo, vlog);

        vlog.setId(vid);

        vlog.setLikeCounts(0);
        vlog.setCommentsCounts(0);
        vlog.setIsPrivate(YesOrNo.NO.type);

        vlog.setCreatedTime(new Date());
        vlog.setUpdatedTime(new Date());

        vlogMapper.insert(vlog);
    }

    @Override
    public PagedGridResult getIndexVlogList(String userId, String search, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);  // 对查询做了一个拦截

        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotBlank(search)) {
            map.put("search", search);
        }
        List<IndexVlogVo> vlogVOList = vlogMapperCustom.getIndexVlogList(map);

        for(IndexVlogVo vlogVo: vlogVOList) {
            String vlogerId = vlogVo.getVlogerId();
            String vlogId = vlogVo.getVlogId();

            // 判断当前用户是否点赞过这个视频
            if(StringUtils.isNotBlank(userId)) {
                // 判断用户是否关注该博主
                boolean doIFollowVloger = fanService.queryDoIFollowVloger(userId, vlogerId);
                vlogVo.setDoIFollowVloger(doIFollowVloger);

                vlogVo.setDoILikeThisVlog(doILikeVlog(userId, vlogId));
            }

            // 判断当前视频的点赞数
            vlogVo.setLikeCounts(getVlogBeLikedCounts(vlogId));
        }
        return setterPagedGrid(vlogVOList, page);
    }

    private boolean doILikeVlog(String myId, String vlogId) {
        String doILike = redis.get(REDIS_USER_LIKE_VLOG+":"+myId+":"+vlogId);
        boolean isLike = false;
        if (StringUtils.isNotBlank(doILike) && doILike.equalsIgnoreCase("1")) {
            isLike = true;
        }
        return isLike;
    }

    @Override
    public Integer getVlogBeLikedCounts(String vlogId) {
        String countsStr = redis.get(REDIS_VLOG_BE_LIKED_COUNTS + ":" + vlogId);
        if (StringUtils.isBlank(countsStr)) {
            countsStr = "0";
        }
        return Integer.valueOf(countsStr);
    }

    @Override
    public IndexVlogVo getVlogDetailById(String userId, String vlogId) {
        Map<String, Object> map = new HashMap<>();
        map.put("vlogId", vlogId);
        List<IndexVlogVo> list = vlogMapperCustom.getVlogDetailById(map);

        if (list != null && list.size() > 0) {
            IndexVlogVo vlogVO = list.get(0);
            return setterVO(vlogVO, userId);  // 要保证和数据库中的内容一致，所以发向前端的数据包括点赞等信息
        }
        return null;
    }

    @Transactional
    @Override
    public void changeToPrivateOrPublic(String userId, String vlogId, Integer yesOrNo) {
        Example example = new Example(Vlog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("vlogerId", userId);
        criteria.andEqualTo("id", vlogId);

        Vlog pendingVlog = new Vlog();
        pendingVlog.setIsPrivate(yesOrNo);

        vlogMapper.updateByExampleSelective(pendingVlog, example);
    }

    @Override
    public PagedGridResult queryMyVlogList(final String userId,
                                           final Integer page,
                                           final Integer pageSize,
                                           final Integer yesOrNo) {
        Example example = new Example(Vlog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("vlogerId", userId);
        criteria.andEqualTo("isPrivate", yesOrNo);

        PageHelper.startPage(page, pageSize);
        List<Vlog> list = vlogMapper.selectByExample(example);
        return setterPagedGrid(list, page);
    }

    @Transactional
    @Override
    public void userLikeVlog(final String userId, final String vlogId) {
        String rid = sid.nextShort();

        MyLikedVlog likedVlog = new MyLikedVlog();
        likedVlog.setId(rid);
        likedVlog.setVlogId(vlogId);
        likedVlog.setUserId(userId);

        myLikedVlogMapper.insert(likedVlog);

        // 系统消息提示：点赞短视频
        Vlog vlog = this.getVlog(vlogId);
        Map msgContent = new HashMap<>();
        msgContent.put("vlogId", vlogId);
        msgContent.put("vlogCover", vlog.getCover());

        msgService.createMsg(userId, vlog.getVlogerId(), MessageEnum.LIKE_VLOG.type, msgContent);
    }

    public Vlog getVlog(String id) {
        return vlogMapper.selectByPrimaryKey(id);
    }

    @Transactional
    @Override
    public void userUnLikeVlog(final String userId, final String vlogId) {
        MyLikedVlog likedVlog = new MyLikedVlog();
        likedVlog.setVlogId(vlogId);
        likedVlog.setUserId(userId);

        myLikedVlogMapper.delete(likedVlog);  // 根据不为空的属性是否相等进行删除
    }

    @Override
    public PagedGridResult getMyLikedVlogList(final String userId, final Integer page, final Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        List<IndexVlogVo> list = vlogMapperCustom.getMyLikedVlogList(map);
        return setterPagedGrid(list, page);
    }

    @Override
    public PagedGridResult getMyFollowVlogList(final String myId, final Integer page, final Integer pageSize) {
        PageHelper.startPage(page, pageSize);

        Map<String, Object> map = new HashMap<>();
        map.put("myId", myId);

        List<IndexVlogVo> list = vlogMapperCustom.getMyFollowVlogList(map);

        for(IndexVlogVo vlogVo: list) {
            // String vlogerId = vlogVo.getVlogerId();
            String vlogId = vlogVo.getVlogId();

            // 判断当前用户是否点赞过这个视频
            if(StringUtils.isNotBlank(myId)) {
                vlogVo.setDoIFollowVloger(true);  // 必定关注了
                vlogVo.setDoILikeThisVlog(doILikeVlog(myId, vlogId));
            }

            // 判断当前视频的点赞数
            vlogVo.setLikeCounts(getVlogBeLikedCounts(vlogId));
        }

        return setterPagedGrid(list, page);
    }

    @Override
    public PagedGridResult getMyFriendVlogList(final String myId, final Integer page, final Integer pageSize) {
        PageHelper.startPage(page, pageSize);

        Map<String, Object> map = new HashMap<>();
        map.put("myId", myId);

        List<IndexVlogVo> list = vlogMapperCustom.getMyFriendVlogList(map);

        // Question：重复代码，是否需要重构？
        for(IndexVlogVo vlogVo: list) {
            // String vlogerId = vlogVo.getVlogerId();
            String vlogId = vlogVo.getVlogId();

            // 判断当前用户是否点赞过这个视频
            if(StringUtils.isNotBlank(myId)) {
                vlogVo.setDoIFollowVloger(true);  // 必定关注了
                vlogVo.setDoILikeThisVlog(doILikeVlog(myId, vlogId));
            }

            // 判断当前视频的点赞数
            vlogVo.setLikeCounts(getVlogBeLikedCounts(vlogId));
            System.out.println(vlogVo);
        }

        return setterPagedGrid(list, page);
    }

    private IndexVlogVo setterVO(IndexVlogVo v, String userId) {
        String vlogerId = v.getVlogerId();
        String vlogId = v.getVlogId();

        if (StringUtils.isNotBlank(userId)) {
            // 用户是否关注该博主
            boolean doIFollowVloger = fanService.queryDoIFollowVloger(userId, vlogerId);
            v.setDoIFollowVloger(doIFollowVloger);

            // 判断当前用户是否点赞过视频
            v.setDoILikeThisVlog(doILikeVlog(userId, vlogId));
        }

        // 获得当前视频被点赞过的总数
        v.setLikeCounts(getVlogBeLikedCounts(vlogId));

        return v;
    }

    @Transactional
    @Override
    public void flushCounts(final String vlogId, final Integer counts) {
        Vlog vlog = new Vlog();
        vlog.setId(vlogId);
        vlog.setLikeCounts(counts);

        vlogMapper.updateByPrimaryKeySelective(vlog);
    }
}
