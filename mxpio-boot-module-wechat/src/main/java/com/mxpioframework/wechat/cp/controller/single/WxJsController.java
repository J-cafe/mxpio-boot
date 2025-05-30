package com.mxpioframework.wechat.cp.controller.single;

import com.mxpioframework.wechat.cp.config.single.WxCpConfiguration;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="https://github.com/0katekate0">Wang_Wong</a>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/wx/cp/js/")
public class WxJsController {
    @Value("${wechat.cp.appConfigs[0].agentId}")
    private Integer agentId;
    @Value("${wechat.cp.corpId}")
    private String corpId;
    @PostMapping("/getJsConf")
    public Map getJsConf(String uri) throws WxErrorException {

        final WxCpService wxCpService = WxCpConfiguration.getCpService(agentId);
        if (wxCpService == null) {
            throw new IllegalArgumentException(String.format("未找到对应agentId=[%d]的配置，请核实！", agentId));
        }

        WxJsapiSignature wxJsapiSignature = wxCpService.createJsapiSignature(uri);
        String signature = wxJsapiSignature.getSignature();
        String nonceStr = wxJsapiSignature.getNonceStr();
        long timestamp = wxJsapiSignature.getTimestamp();

        Map res = new HashMap<String, String>();
        res.put("appId", corpId); // 必填，企业微信的corpID
        res.put("timestamp", timestamp); // 必填，生成签名的时间戳
        res.put("nonceStr", nonceStr); // 必填，生成签名的随机串
        res.put("signature", signature); // 必填，签名，见 附录-JS-SDK使用权限签名算法
        return res;
    }


    public static String genNonce() {
        return bytesToHex(Long.toString(System.nanoTime()).getBytes(StandardCharsets.UTF_8));
    }

    public static String bytesToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

}
