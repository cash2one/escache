package com.cache.ws.es;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Search;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;

public class ESConfiguration {

	private static ESConfiguration singleton = new ESConfiguration();

	@SuppressWarnings("unused")
	private JestClient client;

	private Settings settings;

	private ESConfiguration() {
		settings = ImmutableSettings.settingsBuilder()
				.loadFromClasspath("cache.yml").build();

	}

	public static ESConfiguration getInstance() {
		return singleton;
	}

	public Settings getSettings() {
		return settings;
	}

	public JestClient getClient() {
		String connectionUrl = "http://" + settings.get("ip") + ":"
				+ settings.get("port");

		JestClientFactory factory = new JestClientFactory();

		factory.setHttpClientConfig(new HttpClientConfig.Builder(connectionUrl)
				.multiThreaded(true).build());
		JestClient client = factory.getObject();
		return client;
	}

	public void setClient(JestClient client) {
		this.client = client;
	}

	public static void main(String[] args) throws Exception {

		JestClient client = ESConfiguration.getInstance().getClient();

		String query = "{\n" + "    \"query\": {\n"
				+ "        \"filtered\" : {\n" + "            \"query\" : {\n"
				+ "                \"query_string\" : {\n"
				+ "                    \"query\" : \"宣城市\"\n"
				+ "                }\n" + "            }\n" + "        }\n"
				+ "    }\n" + "}";

		Search search = (Search) new Search.Builder(query)
		// multiple index or types can be added.
				.addIndex("access-2015-07-16").addType("1").build();

		JestResult result = client.execute(search);
		

		// SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		// searchSourceBuilder.query(QueryBuilders.queryStringQuery("宣城市"));
		//
		// Search search = (Search) new Search.Builder(
		// searchSourceBuilder.toString())
		// .addIndex("access-2015-07-16").addType("1").build();
		//
		// JestResult result = client.execute(search);

		System.out.print(result.getJsonString());

	}

}
