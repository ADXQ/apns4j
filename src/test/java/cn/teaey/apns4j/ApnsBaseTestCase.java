package cn.teaey.apns4j;

import cn.teaey.apns4j.network.ApnsChannel;
import cn.teaey.apns4j.network.ApnsChannelFactory;
import cn.teaey.apns4j.network.async.ApnsService;

/**
 * @author teaey(xiaofei.wxf)
 * @since 1.0.3
 */
public class ApnsBaseTestCase {
    final ApnsChannelFactory apnsChannelFactory = Apns4j.newApnsChannelFactoryBuilder().keyStoreMeta(TestConts.keyStorePath).keyStormPwd(TestConts.keyStorePwd).build();
    final ApnsChannel apnsChannel = apnsChannelFactory.newChannel();
    final ApnsService apnsService = new ApnsService(3, apnsChannelFactory, 3);


}
