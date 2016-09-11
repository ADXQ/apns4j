package cn.teaey.apns4j;

import cn.teaey.apns4j.network.ApnsChannelFactory;
import cn.teaey.apns4j.network.ApnsChannel;
import cn.teaey.apns4j.network.ApnsGateway;
import cn.teaey.apns4j.protocol.NotifyPayload;
import org.junit.Test;

/**
 * @author teaey(xiaofei.wxf)
 * @since 1.0.3
 */
public class SyncInvokeTest {
    /**
     * Very Simple to use APNS4j
     */
    @Test
    public void rawInvoke() {
        ApnsChannelFactory factory = Apns4j.apnsChannelFactory().keyStoreWrapper(Apns4j.buildKeyStoreWrapper(TestConts.keyStorePath, TestConts.keyStorePwd))
                .apnsServer(ApnsGateway.DEVELOPMENT).build();
        ApnsChannel apnsChannel = factory.newChannel();

        //create & init notify payload
        NotifyPayload notifyPayload = Apns4j.buildNotifyPayload()
                .alertBody("Test Test")
//                .alert(Long.toString(""System.currentTimeMillis()))
                .badge(0);
        //notifyPayload.sound("default").alertBody("Pushed By \\\" apns4j").alertActionLocKey("Button Text");

        apnsChannel.sendAndFlush(TestConts.deviceToken, notifyPayload);
        apnsChannel.sendAndFlush(TestConts.deviceToken2, notifyPayload);

        //maybe more send operations

        //in the end
        apnsChannel.close();
    }
}
