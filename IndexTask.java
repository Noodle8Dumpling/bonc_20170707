package cn.com.bonc;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;

import cn.com.bonc.config.Conf;
import cn.com.bonc.dataprocess.Processor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

public class IndexTask implements Runnable {

	private TransportClient client = null;
	private BulkProcessor bulkProcessor = null;
	private Conf conf;
	private KafkaStream<byte[], byte[]> stream;
	private String threadNo;
	private HashMap<Integer, String> fields = new HashMap<Integer, String>();
	private String mainKey = null;
	private int mainKeyIndex = 0;
	private String seperator = null;
	private Processor<String> dataprocessor = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

	@SuppressWarnings("unchecked")
	public IndexTask(KafkaStream<byte[], byte[]> stream, final String threadNo,
			Conf conf) {
		this.conf = conf;
		this.stream = stream;
		this.threadNo = threadNo;
		this.mainKey = conf.esMainKey;
		this.seperator = conf.esFieldSeperator;
		Settings settings = Settings.settingsBuilder()
				.put("cluster.name", conf.esClusterName).build();
		try {
			client = TransportClient.builder().settings(settings).build();
			client.addTransportAddress(new InetSocketTransportAddress(
					InetAddress.getByName(conf.esHost), Integer
							.valueOf(conf.esPort)));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		/*
		 * In constructor, to figure out the fields,which user defines
		 */
		String fieldsUerDefine = conf.esFileds;
		String cols[] = StringUtils.split(fieldsUerDefine, ",");
		for (int i = 0; i < cols.length; i++) {
			this.fields.put(i, StringUtils.split(cols[i], "(")[0]);
			if (StringUtils.split(cols[i], "(")[0].equals(this.mainKey))
				this.mainKeyIndex = i;
		}

		// get dataprocessor
		try {
			this.dataprocessor = (Processor<String>) Class.forName(
					conf.processor).newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		bulkProcessor = BulkProcessor
				.builder(client, new BulkProcessor.Listener() {
					@Override
					public void beforeBulk(long executionId, BulkRequest request) {
					}

					@Override
					public void afterBulk(long executionId,
							BulkRequest request, BulkResponse response) {
						System.out.println(threadNo + "提交"
								+ response.getItems().length + "个文档，用时"
								+ response.getTookInMillis() + "MS"
								+ (response.hasFailures() ? " 有文档提交失败！" : ""));
						System.out.println("response"
								+ response.buildFailureMessage());
					}

					@Override
					public void afterBulk(long executionId,
							BulkRequest request, Throwable failure) {
						System.out.println(" 有文档提交失败！after failure=" + failure);
					}
				})
				.setBulkActions(10000)
				.setBulkSize(new ByteSizeValue(1, ByteSizeUnit.GB))
				.setFlushInterval(TimeValue.timeValueSeconds(5))
				.setConcurrentRequests(1)
				.setBackoffPolicy(
						BackoffPolicy.exponentialBackoff(
								TimeValue.timeValueMillis(100), 3)).build();

	}

	@Override
	public void run() {

		int flag = -1;

		String _id = "";

		String line;
		System.out.println("CURRENT THREAD NO IS " + threadNo);
		long countLines = 0;
		LinkedHashMap<String, Object> json = new LinkedHashMap<String, Object>();
		try {
			ConsumerIterator<byte[], byte[]> iterator = stream.iterator();
			// System.out.println(iterator.size());
			while (iterator.hasNext()) {
				// use user define data processor to process each line
				line = this.dataprocessor.process(new String(iterator.next()
						.message()));

				System.out.println(line);
				countLines++;
				String[] colus = StringUtils
						.splitByWholeSeparatorPreserveAllTokens(line,
								this.seperator);

				try {

					for (int i = 0; i < colus.length; i++) {
						json.put(fields.get(i), colus[i]);
						System.out.println(fields.get(i)+":"+colus[i]);
					}

					if (this.mainKey == null || this.mainKey.equals("")) {
						_id = sdf.format(new Date())
								+ UUID.randomUUID().toString()
										.replaceAll("-", "");
					} else {
						_id = colus[this.mainKeyIndex];
					}

					bulkProcessor.add(new IndexRequest(conf.esIndexName,
							conf.esTypeName, _id).source(json).routing(_id));
					System.out.println(json);
					if (countLines % 10000 == 0) {
						bulkProcessor.flush();
						countLines = 0;
						 
					}
					bulkProcessor.flush();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
