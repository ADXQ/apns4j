package cn.teaey.apns4j;

import cn.teaey.apns4j.protocol.ApnsPayload;
import org.junit.Test;

/**
 * @author teaey(xiaofei.wxf)
 * @since 1.0.3
 */
public class RawUsageTest extends ApnsBaseTestCase {

    @Test
    public void alert1() throws InterruptedException {

        //create & init notify payload
        ApnsPayload apnsPayload = Apns4j.newApnsPayload()
                .alert("Pushed by apns4j")
                .sound("default");

        //send via channel
        apnsChannel.sendAndFlush(TestConts.deviceToken, apnsPayload);

        Thread.sleep(3000);
        System.out.println(apnsChannel.recvErrorResp());;
//        apnsChannel.sendAndFlush(TestConts.deviceToken2, apnsPayload);

        //in the end
        apnsChannel.close();

    }
}
