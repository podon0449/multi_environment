package com.multi.util.slack;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SlackClient {
    //Http 기반으로 처리
    /*public static void slackSend(String message, HtSlackType type)  {
        //운영계인 경우만 처리.
        if (!DevelopEnvSwitcher.isDevelopEnv()) {
            Map<String, Object> data = new HashMap<>();
            data.put("text", message);
            try {
                new HanteoHttpClient().build(type.getUrl())
                        .post(new StringEntity(JsonUtils.toJson(data), "UTF-8"), getJsonHeader()).getRespones();
            } catch (URISyntaxException e) {
                logger.error("error  URISyntaxException error : " + e.getMessage());
            } catch (ClientProtocolException e) {
                logger.error("error  ClientProtocolException error : " + e.getMessage());
            }
        logger.warn("slackSend :", message);
        }

        else {
            logger.warn("slackSend :", message);
        }
    }*/

}
