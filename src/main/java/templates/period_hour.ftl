{
  "size": 0,
  "aggs": {
    "result": {
      "date_histogram": {
        "field": "utime",
        "interval": "1h",
        "format": "HH",
        "time_zone": "+08:00",
        "order": {
          "_key": "asc"
        },
        "min_doc_count": 0,
        "extended_bounds": {
          "min": ${start},
      	  "max": ${end}
        }
      },
      "aggs": {
        "pv": {
          "value_count": {
            "field": "loc"
          }
        },
        "uv": {
          "cardinality": {
            "field": "_ucv"
          }
        },
        "vc": {
          "filter": {
            "term": {
              "entrance": "1"
            }
          },
          "aggs": {
            "aggs": {
              "value_count": {
                "field": "tt"
              }
            }
          }
        },
        "single_visitor_aggs": {
          "terms": {
            "field": "tt",
            "size": 0,
            "min_doc_count": 2
          }
        },
        "tvt_aggs": {
          "terms": {
            "field": "tt",
            "size": 0
          },
          "aggs": {
            "min_aggs": {
              "min": {
                "field": "utime"
              }
            },
            "max_aggs": {
              "max": {
                "field": "utime"
              }
            }
          }
        },
        "new_visitor_aggs": {
          "filter": {
            "term": {
              "entrance": "1"
            }
          },
          "aggs": {
            "aggs": {
              "sum": {
                "script": "c=0; if (doc['ct'].value == 0) {c = 1}; c"
              }
            }
          }
        },
        "uv_filter": {
          "filter": {
            "term": {
              "entrance": "1"
            }
          },
          "aggs": {
            "aggs": {
              "cardinality": {
                "field": "_ucv"
              }
            }
          }
        },
        "ip_aggs": {
          "filter": {
            "term": {
              "ip_dupli": 1
            }
          },
          "aggs": {
            "aggs": {
              "value_count": {
                "field": "remote"
              }
            }
          }
        }
      }
    }
  }
}