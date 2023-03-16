package org.example.mufiye.mo;

import java.util.Date;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

// mo: mongodb object
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document("message")
public class MessageMo {
    @Id
    private String id;  // 主键id

    @Field("fromUserId")
    private String fromUserId;  // 消息来自的用户id

    @Field("fromNickname")
    private String fromNickname;

    @Field("fromFace")
    private String fromFace;

    @Field("toUserId")
    private String toUserId;

    @Field("msgType")
    private Integer msgType;  // 消息类型

    @Field("msgContent")
    private Map msgContent;  // 消息内容

    @Field("createTime")
    private Date createTime;  // 消息创建时间
}
