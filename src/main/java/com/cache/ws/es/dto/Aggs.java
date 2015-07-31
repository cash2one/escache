package com.cache.ws.es.dto;
import java.util.List;
import java.util.Map;

public class Aggs {

		private EsValue aggs;

		private List<Map<String, Object>> buckets;

		public List<Map<String, Object>> getBuckets() {
			return buckets;
		}

		public void setBuckets(List<Map<String, Object>> buckets) {
			this.buckets = buckets;
		}

		public EsValue getAggs() {
			return aggs;
		}

		public void setAggs(EsValue aggs) {
			this.aggs = aggs;
		}

	}
