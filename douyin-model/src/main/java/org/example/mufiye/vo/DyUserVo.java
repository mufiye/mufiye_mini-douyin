package org.example.mufiye.vo;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DyUserVo {
    private String id;
    private String mobile;
    private String nickname;
    private String douyinId;
    private String face;
    private Integer sex;
    private Date birthday;
    private String country;
    private String province;
    private String city;
    private String district;
    private String description;
    private String bgImg;
    private Integer canDouyinIdBeUpdated;
    private Date createdTime;
    private Date updatedTime;

    private String userToken;

    private Integer myFollowsCounts;
    private Integer myFansCounts;
    //    private Integer myLikedVlogCounts;
    private Integer totalLikeMeCounts;
}
