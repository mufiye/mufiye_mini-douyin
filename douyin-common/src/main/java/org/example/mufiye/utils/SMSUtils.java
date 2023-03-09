package org.example.mufiye.utils;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SMSUtils {

    private TencentCloudProperties tencentCloudProperties;

    @Autowired
    public void setTencentCloudProperties(TencentCloudProperties tencentCloudProperties) {
        this.tencentCloudProperties = tencentCloudProperties;
    }

    public void sendSMS(String phone, String code) throws Exception {
        try {
            Credential cred = new Credential(tencentCloudProperties.getSecretId(),
                                             tencentCloudProperties.getSecretKey());
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("sms.tencentcloudapi.com");
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            SmsClient client = new SmsClient(cred, "ap-guangzhou", clientProfile);

            SendSmsRequest request = new SendSmsRequest();
            String[] phoneNumberSet1 = {"+86" + phone};
            request.setPhoneNumberSet(phoneNumberSet1);
            // 根据控制台信息自定义
            // https://cloud.tencent.com/document/product/382/55981
            request.setSmsSdkAppId("1400800682"); // https://console.cloud.tencent.com/smsv2/app-manage
            request.setSignName("myLastDance"); // https://console.cloud.tencent.com/smsv2/csms-sign
            request.setTemplateId("1723751");

            String[] templateParaSet = {code};
            request.setTemplateParamSet(templateParaSet);

            SendSmsResponse response = client.SendSms(request);
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
    }

//    public static void main(String[] args) {
//        try {
//
//        } catch () {
//
//        }
//    }
}
