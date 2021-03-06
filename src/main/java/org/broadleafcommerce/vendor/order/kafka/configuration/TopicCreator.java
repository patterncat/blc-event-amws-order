package org.broadleafcommerce.vendor.order.kafka.configuration;

import java.util.Properties;

import kafka.admin.AdminUtils;
import kafka.common.TopicExistsException;
import kafka.utils.ZkUtils;
import kafka.utils.ZKStringSerializer$;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.context.SmartLifecycle;

/**
 * Create topic for KAFKA.
 * 
 */
public class TopicCreator implements SmartLifecycle {

	private final String topic;

	private final String zkConnect;

	private volatile boolean running;

	public TopicCreator(String topic, String zkConnect) {
		this.topic = topic;
		this.zkConnect = zkConnect;
	}

	@Override
	public void start() {
		ZkUtils zkUtils = new ZkUtils(new ZkClient(this.zkConnect, 6000, 6000,
			ZKStringSerializer$.MODULE$), null, false);
		try {
			AdminUtils.createTopic(zkUtils, topic, 1, 1, new Properties());
		}
		catch (TopicExistsException e) {
			// no-op
		}
		this.running = true;
	}

	@Override
	public void stop() {
	}

	@Override
	public boolean isRunning() {
		return this.running;
	}

	@Override
	public int getPhase() {
		return Integer.MIN_VALUE;
	}

	@Override
	public boolean isAutoStartup() {
		return true;
	}

	@Override
	public void stop(Runnable callback) {
		callback.run();
	}

}