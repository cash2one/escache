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
          "min": "1437235200000",
          "max": "1437321599999"
        }
      },
      "aggs": {
        "pv": {
          "value_count": {
            "field": "loc"
          }
        }
      }
    }
  }
}