package org.example.mufiye.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.mufiye.base.BaseInfoProperties;
import org.example.mufiye.bo.VlogBo;
import org.example.mufiye.enums.YesOrNo;
import org.example.mufiye.result.GraceJSONResult;
import org.example.mufiye.service.VlogService;
import org.example.mufiye.utils.PagedGridResult;
import org.example.mufiye.vo.IndexVlogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "Vlog Controller 视频相关的业务")
@RequestMapping("vlog")
@RestController
@RefreshScope
public class VlogController extends BaseInfoProperties {
    @Autowired
    VlogService vlogService;

    // 视频具体文件的上传在前端cdn进行，后端只是存储视频信息
    @PostMapping("publish")
    public GraceJSONResult publish(@RequestBody VlogBo vlogBo) {
        vlogService.createVlog(vlogBo);
        return GraceJSONResult.ok();
    }

    @GetMapping("indexList")
    public GraceJSONResult getList(@RequestParam(defaultValue = "") String userId,
                                   @RequestParam(defaultValue = "") String search,
                                   @RequestParam Integer page,
                                   @RequestParam Integer pageSize) {
        if (page == null) {
            page = COMMON_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult pagedGridResult = vlogService.getIndexVlogList(userId, search, page, pageSize);
        return GraceJSONResult.ok(pagedGridResult);
    }

    @GetMapping("detail")
    public GraceJSONResult detail(@RequestParam(defaultValue = "") String userId,
                                  @RequestParam(defaultValue = "") String vlogId) {
        IndexVlogVo vlogVO = vlogService.getVlogDetailById(userId, vlogId);
        return GraceJSONResult.ok(vlogVO);
    }

    @PostMapping("changeToPrivate")
    public GraceJSONResult changeToPrivate(@RequestParam(defaultValue = "") String userId,
                                           @RequestParam(defaultValue = "") String vlogId) {
        vlogService.changeToPrivateOrPublic(userId, vlogId, YesOrNo.YES.type);
        return GraceJSONResult.ok();
    }

    @PostMapping("changeToPublic")
    public GraceJSONResult changeToPublic(@RequestParam(defaultValue = "") String userId,
                                          @RequestParam(defaultValue = "") String vlogId) {
        vlogService.changeToPrivateOrPublic(userId, vlogId, YesOrNo.NO.type);
        return GraceJSONResult.ok();
    }

    @GetMapping("myPublicList")
    public GraceJSONResult myPublicList(@RequestParam String userId,
                                        @RequestParam Integer page,
                                        @RequestParam Integer pageSize) {

        if (page == null) {
            page = COMMON_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult gridResult = vlogService.queryMyVlogList(
            userId,
            page,
            pageSize,
            YesOrNo.NO.type
        );
        return GraceJSONResult.ok(gridResult);
    }

    @GetMapping("myPrivateList")
    public GraceJSONResult myPrivateList(@RequestParam String userId,
                                         @RequestParam Integer page,
                                         @RequestParam Integer pageSize) {

        if (page == null) {
            page = COMMON_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult gridResult = vlogService.queryMyVlogList(
            userId,
            page,
            pageSize,
            YesOrNo.YES.type
        );
        return GraceJSONResult.ok(gridResult);
    }

    @Value("${nacos.counts}")
    private Integer nacosCounts;

    @PostMapping("like")
    public GraceJSONResult like(@RequestParam String userId,
                                @RequestParam String vlogerId,
                                @RequestParam String vlogId) {
        vlogService.userLikeVlog(userId, vlogId);

        // 点赞数修改
        redis.increment(REDIS_VLOG_BE_LIKED_COUNTS + ":" + vlogId, 1);
        redis.increment(REDIS_VLOGER_BE_LIKED_COUNTS + ":" + vlogerId, 1);

        // 我点赞过的视频, 需要在redis中保存记录
        redis.set(REDIS_USER_LIKE_VLOG + ":" + userId + ":" + vlogId, "1");

        // 超过一定阈值点赞信息就刷新入库
        String countsStr = redis.get(REDIS_VLOGER_BE_LIKED_COUNTS + ":" + vlogerId);
        Integer counts = 0;
        log.info("nacos配置的count:" + nacosCounts);
        if (StringUtils.isNotBlank(countsStr)) {
            counts = Integer.valueOf(countsStr);
            if(counts >= nacosCounts) {
                log.info("刷新入库");
                vlogService.flushCounts(vlogId, counts);
            }
        }
        return GraceJSONResult.ok();
    }

    @PostMapping("unlike")
    public GraceJSONResult unlike(@RequestParam String userId,
                                  @RequestParam String vlogerId,
                                  @RequestParam String vlogId) {
        // 取消点赞的视频，关联关系删除
        vlogService.userUnLikeVlog(userId, vlogId);

        // 点赞数修改，减少
        redis.decrement(REDIS_VLOG_BE_LIKED_COUNTS + ":" + vlogId, 1);
        redis.decrement(REDIS_VLOGER_BE_LIKED_COUNTS + ":" + vlogerId, 1);

        // 删除之前关于我点赞过的视频的redis记录
        redis.del(REDIS_USER_LIKE_VLOG + ":" + userId + ":" + vlogId);

        return GraceJSONResult.ok();
    }

    @PostMapping("totalLikedCounts")
    public GraceJSONResult totalLikedCounts(@RequestParam String vlogId) {
        return GraceJSONResult.ok(vlogService.getVlogBeLikedCounts(vlogId));
    }

    @RequestMapping("myLikedList")
    public GraceJSONResult myLikedList(@RequestParam String userId,
                                       @RequestParam Integer page,
                                       @RequestParam Integer pageSize) {
        if (page == null) {
            page = COMMON_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult gridResult = vlogService.getMyLikedVlogList(
            userId,
            page,
            pageSize
        );
        return GraceJSONResult.ok(gridResult);
    }

    @GetMapping("followList")
    public GraceJSONResult followList(@RequestParam String myId,
                                      @RequestParam Integer page,
                                      @RequestParam Integer pageSize) {
        if (page == null) {
            page = COMMON_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult gridResult = vlogService.getMyFollowVlogList(
            myId,
            page,
            pageSize
        );
        return GraceJSONResult.ok(gridResult);
    }

    @GetMapping("friendList")
    public GraceJSONResult friendList(@RequestParam String myId,
                                      @RequestParam Integer page,
                                      @RequestParam Integer pageSize) {
        if (page == null) {
            page = COMMON_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult gridResult = vlogService.getMyFriendVlogList(
            myId,
            page,
            pageSize
        );
        return GraceJSONResult.ok(gridResult);
    }
}
