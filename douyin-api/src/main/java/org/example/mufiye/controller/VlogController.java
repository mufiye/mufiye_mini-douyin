package org.example.mufiye.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "Vlog Controller 视频模块的接口")
@RequestMapping("vlog")
@RestController
@RefreshScope
public class VlogController extends BaseInfoProperties {
    @Autowired
    VlogService vlogService;

    // 视频具体文件的上传在前端cdn进行，后端只是存储视频信息
    @ApiOperation("上传视频信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "vlogBo", value = "视频信息", required = true, paramType = "body", dataType = "VlogBo")
    })
    @PostMapping("publish")
    public GraceJSONResult publish(@RequestBody VlogBo vlogBo) {
        vlogService.createVlog(vlogBo);
        return GraceJSONResult.ok();
    }

    @ApiOperation("获取视频列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "userId", value = "用户id,用于获取某用户的视频", required = false, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "search", value = "搜索的关键词", required = false, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "page", value = "页号", required = true, paramType = "query", dataType = "Integer"),
        @ApiImplicitParam(name = "pageSize", value = "页面条目数", required = true, paramType = "query", dataType = "Integer"),
    })
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

    @ApiOperation("获取具体的某个视频")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "userId", value = "用户id,用于获取某用户的视频", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "vlogId", value = "视频id", required = true, paramType = "query", dataType = "String")
    })
    @GetMapping("detail")
    public GraceJSONResult detail(@RequestParam(defaultValue = "") String userId,
                                  @RequestParam(defaultValue = "") String vlogId) {
        IndexVlogVo vlogVO = vlogService.getVlogDetailById(userId, vlogId);
        return GraceJSONResult.ok(vlogVO);
    }

    @ApiOperation("用户将某个视频改为私密的")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "vlogId", value = "视频id", required = true, paramType = "query", dataType = "String")
    })
    @PostMapping("changeToPrivate")
    public GraceJSONResult changeToPrivate(@RequestParam(defaultValue = "") String userId,
                                           @RequestParam(defaultValue = "") String vlogId) {
        vlogService.changeToPrivateOrPublic(userId, vlogId, YesOrNo.YES.type);
        return GraceJSONResult.ok();
    }

    @ApiOperation("用户将某个视频改为公开的")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "vlogId", value = "视频id", required = true, paramType = "query", dataType = "String")
    })
    @PostMapping("changeToPublic")
    public GraceJSONResult changeToPublic(@RequestParam(defaultValue = "") String userId,
                                          @RequestParam(defaultValue = "") String vlogId) {
        vlogService.changeToPrivateOrPublic(userId, vlogId, YesOrNo.NO.type);
        return GraceJSONResult.ok();
    }

    @ApiOperation("用户查看自己的公开视频列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "page", value = "页号", required = true, paramType = "query", dataType = "Integer"),
        @ApiImplicitParam(name = "pageSize", value = "页面条目数", required = true, paramType = "query", dataType = "Integer")
    })
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

    @ApiOperation("用户查看自己的私密视频列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "page", value = "页号", required = true, paramType = "query", dataType = "Integer"),
        @ApiImplicitParam(name = "pageSize", value = "页面条目数", required = true, paramType = "query", dataType = "Integer")
    })
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

    @ApiOperation("用户点赞某个视频")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "vlogerId", value = "发布视频者的id", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "vlogId", value = "视频id", required = true, paramType = "query", dataType = "String")
    })
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

    @ApiOperation("用户取消点赞某个视频")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "vlogerId", value = "发布视频者的id", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "vlogId", value = "视频id", required = true, paramType = "query", dataType = "String")
    })
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

    @ApiOperation("获取某个视频的点赞总数")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "vlogId", value = "视频id", required = true, paramType = "query", dataType = "String")
    })
    @PostMapping("totalLikedCounts")
    public GraceJSONResult totalLikedCounts(@RequestParam String vlogId) {
        return GraceJSONResult.ok(vlogService.getVlogBeLikedCounts(vlogId));
    }

    @ApiOperation("用户查看自己点赞过的视频")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "page", value = "页号", required = true, paramType = "query", dataType = "Integer"),
        @ApiImplicitParam(name = "pageSize", value = "页面条目数", required = true, paramType = "query", dataType = "Integer")
    })
    @GetMapping("myLikedList")
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

    @ApiOperation("用户查看自己的关注用户的视频列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "myId", value = "用户自己的id", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "page", value = "页号", required = true, paramType = "query", dataType = "Integer"),
        @ApiImplicitParam(name = "pageSize", value = "页面条目数", required = true, paramType = "query", dataType = "Integer")
    })
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

    @ApiOperation("用户查看自己的朋友的视频列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "myId", value = "用户自己的id", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "page", value = "页号", required = true, paramType = "query", dataType = "Integer"),
        @ApiImplicitParam(name = "pageSize", value = "页面条目数", required = true, paramType = "query", dataType = "Integer")
    })
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
