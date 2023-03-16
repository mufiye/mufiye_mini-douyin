package org.example.mufiye.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.mufiye.base.BaseInfoProperties;
import org.example.mufiye.bo.UpdatedDyUserBo;
import org.example.mufiye.config.MinIOConfig;
import org.example.mufiye.enums.FileTypeEnum;
import org.example.mufiye.enums.UserInfoModifyType;
import org.example.mufiye.pojo.DyUser;
import org.example.mufiye.result.GraceJSONResult;
import org.example.mufiye.result.ResponseStatusEnum;
import org.example.mufiye.service.UserService;
import org.example.mufiye.utils.MinIOUtils;
import org.example.mufiye.vo.DyUserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Api(tags = "User Info Controller 用户个人信息模块的接口")
@RequestMapping("userInfo")
@RestController
public class UserInfoController extends BaseInfoProperties {
    @Autowired
    private UserService userService;

    @ApiOperation("查询用户信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType="String")
    })
    @GetMapping("query")
    public Object query(@RequestParam String userId) {
        DyUser user = userService.getUser(userId);

        DyUserVo usersVO = new DyUserVo();
        BeanUtils.copyProperties(user, usersVO);

        // 我的关注博主总数量
        String myFollowsCountsStr = redis.get(REDIS_MY_FOLLOWS_COUNTS + ":" + userId);
        // 我的粉丝总数
        String myFansCountsStr = redis.get(REDIS_MY_FANS_COUNTS + ":" + userId);
        // 用户获赞总数，视频博主（点赞/喜欢）总和
//        String likedVlogCountsStr = redis.get(REDIS_VLOG_BE_LIKED_COUNTS + ":" + userId);
        String likedVlogerCountsStr = redis.get(REDIS_VLOGER_BE_LIKED_COUNTS + ":" + userId);

        Integer myFollowsCounts = 0;
        Integer myFansCounts = 0;
        Integer likedVlogCounts = 0;
        Integer likedVlogerCounts = 0;
        Integer totalLikeMeCounts = 0;

        if (StringUtils.isNotBlank(myFollowsCountsStr)) {
            myFollowsCounts = Integer.valueOf(myFollowsCountsStr);
        }
        if (StringUtils.isNotBlank(myFansCountsStr)) {
            myFansCounts = Integer.valueOf(myFansCountsStr);
        }
//        if (StringUtils.isNotBlank(likedVlogCountsStr)) {
//            likedVlogCounts = Integer.valueOf(likedVlogCountsStr);
//        }
        if (StringUtils.isNotBlank(likedVlogerCountsStr)) {
            likedVlogerCounts = Integer.valueOf(likedVlogerCountsStr);
        }

        totalLikeMeCounts = likedVlogCounts + likedVlogerCounts;

        usersVO.setMyFollowsCounts(myFollowsCounts);
        usersVO.setMyFansCounts(myFansCounts);
        usersVO.setTotalLikeMeCounts(totalLikeMeCounts);

        return GraceJSONResult.ok(usersVO);
    }

    @ApiOperation("修改用户信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "updatedDyUserBo", value = "想要修改为的个人信息", required = true, paramType = "body"),
        @ApiImplicitParam(name = "type", value = "指修改的是哪种个人信息", required = true, paramType = "query", dataType="Integer")
    })
    @PostMapping("modifyUserInfo")
    public GraceJSONResult modifyUserInfo(@RequestBody UpdatedDyUserBo updatedDyUserBo,
                                          @RequestParam Integer type) throws Exception{
        UserInfoModifyType.checkUserInfoTypeIsRight(type);
        DyUser dyUser = userService.updateUserInfo(updatedDyUserBo, type);
        return GraceJSONResult.ok(dyUser);
    }

    @Autowired
    private MinIOConfig minIOConfig;

    @ApiOperation("修改头像或背景图片")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "type", value = "修改头像还是背景", required = true, paramType = "query", dataType="Integer"),
        @ApiImplicitParam(name = "file", value = "上传的图片文件", required = true, paramType = "formData")
    })
    @PostMapping("modifyImage")
    public GraceJSONResult modifyImage(@RequestParam String userId,
                                       @RequestParam Integer type,
                                       MultipartFile file) throws Exception {

        if (type != FileTypeEnum.BGIMG.type && type != FileTypeEnum.FACE.type) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }

        String fileName = file.getOriginalFilename();

        MinIOUtils.uploadFile(minIOConfig.getBucketName(),
                              fileName,
                              file.getInputStream());

        String imgUrl = minIOConfig.getFileHost()
            + "/"
            + minIOConfig.getBucketName()
            + "/"
            + fileName;


        // 修改图片地址到数据库
        UpdatedDyUserBo updatedDyUserBo = new UpdatedDyUserBo();
        updatedDyUserBo.setId(userId);

        if (type == FileTypeEnum.BGIMG.type) {
            updatedDyUserBo.setBgImg(imgUrl);
        } else {
            updatedDyUserBo.setFace(imgUrl);
        }
        DyUser dyUser = userService.updateUserInfo(updatedDyUserBo);

        return GraceJSONResult.ok(dyUser);
    }
}
