package cn.com.bonc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.com.bonc.config.Conf;
import cn.com.bonc.dataprocess.Processor;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;


public class KafkaIndexer extends Indexer {

	
	private Conf conf = null;

	private ConsumerConnector consumer;
	private ExecutorService executor;

	public KafkaIndexer(Conf conf) {
		this.conf = conf;

	}
	public KafkaIndexer( ) {
		
	}

	 
	@Override
	public void init() {
		

		Properties props = new Properties();
		props.put("auto.offset.reset", "smallest"); // 必须要加，如果要读旧数据。
													// 默认是largest，即最新，所以不加这个配置，你是读不到你之前produce的数据的
		props.put("zookeeper.connect", conf.kafkaZookeeperHosts);
		props.put("group.id", conf.kafkaGroupID);
		props.put("zookeeper.session.timeout.ms", "6000");// 连接等待6s
		props.put("zookeeper.sync.time.ms", "2000");
		props.put("auto.commit.interval.ms", "1000");
		props.put("socket.receive.buffer.bytes", "102400");
		props.put("max.message.bytes", "1000000");

		this.consumer = kafka.consumer.Consumer.createJavaConsumerConnector(new ConsumerConfig(props));

	}                                                                                                                                                                                             

	@Override
	public void run() {

		 System.out.println("0000000000000000000000000");
	        Map<String, Integer> topicToThreads = new HashMap<String,Integer>();
	        topicToThreads.put(conf.kafkaTopic,conf.kafkaTopicPartitions);
	        System.out.println("conf.kafkaTopicPartitions:"+conf.kafkaTopicPartitions);
	        
	        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicToThreads);
	        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(conf.kafkaTopic);

	        // 启动所有线程
	        executor = Executors.newFixedThreadPool(conf.kafkaTopicPartitions);

	        // 消费
	        int threadNo = 1;
	        for (final KafkaStream<byte[], byte[]> stream : streams) {
	            executor.submit(new IndexTask(stream, String.format("%02d", threadNo),conf));
	            threadNo++;
	        }
	    
	}


	
}
