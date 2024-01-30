# Spring-Boot-Elastic-Search
Explore the power of Elasticsearch in a Spring Boot environment with this demo repository. Learn how to set up Elasticsearch configurations, integrate Spring Data Elasticsearch, and perform common operations like indexing, searching, and filtering. The project provides a hands-on example of leveraging ES capabilities within a Spring Boot app.


## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

## Prerequisites

Before you begin, ensure you have met the following requirements:

- Java Development Kit (JDK) installed
- Maven installed
- Elasticsearch installed (see installation instructions below)

## Installing Elasticsearch

For installing elastic search with debian package you just need to execute this following command
- sudo apt-get install apt-transport-https
- echo "deb [signed-by=/usr/share/keyrings/elasticsearch-keyring.gpg] https://artifacts.elastic.co/packages/8.x/apt stable main" | sudo tee /etc/apt/sources.list.d/elastic-8.x.list
- sudo apt-get update && sudo apt-get install elasticsearch
- sudo /bin/systemctl daemon-reload
- sudo /bin/systemctl enable elasticsearch.service
- sudo systemctl start elasticsearch.service
- sudo systemctl status elasticsearch.service

 Elasticsearch should now be running on `http://localhost:9200`

For data visualization you can install kibana.just this run command
- sudo apt-get update && sudo apt-get install kibana
- sudo /bin/systemctl daemon-reload
- sudo /bin/systemctl enable kibana.service
- sudo systemctl start kibana.service
- sudo systemctl status kibana.service

Kibana should now be running on `http://localhost:5601`
Kibana loads its configuration from the /etc/kibana/kibana.yml file by default

You can follow this link for better understanding how to install elasticsearch & kibana and how to configure them
1. `https://www.elastic.co/guide/en/elasticsearch/reference/current/deb.html`
2. `https://www.elastic.co/guide/en/kibana/current/deb.html`


### Running the Spring Boot Application

1. **Clone the Repository:**

   https://github.com/milton464/spring-boot-elastic-search.git
2. **Navigate to the Project Directory:**

   cd spring-boot-elasticsearch
3. **Build and Run the Application:**

   mvn spring-boot:run
      
The Spring Boot application should be accessible at `http://localhost:8080`

## ElasticSearch Queries

** Full-text Queries: **  In Elasticsearch, full-text queries are used to perform text-based search operations on full-text fields.


  i). ** Match Query: **   The "match" query is a versatile and widely used full-text query. It analyzes the provided text and searches for matching terms in the specified field.
  
  ```
  {
  "query": {
    "match": {
      "first_name": "first"
    }
  }
}
  ```
  
  java equivalend code:
  
  ```
  public static Query getMatchQuery(String fieldName, String fieldVale) {
		return new Query.Builder().match(m -> m.field(fieldName).query(fieldVale)).build();
	}
```

ii). ** Match Phrase Query: **  The "match_phrase" query matches exact phrases in a field. It analyzes the text and searches for the exact phrase in the specified field

```
{
  "query": {
    "match_phrase": {
      "content": "quick brown fox"
    }
  }
}
```

java equivalend code:

```
	public static Query getMatchPhraseQuery(String fieldName, String fieldValue) {
		return new Query.Builder().matchPhrase(m -> m.field(fieldName).query(fieldValue)).build();
	}
```
	
iii). ** Match Phrase Prefix Query:** This query behaves in the same way as the match_phrase query, except that it treats the last word in the query string as a prefix

```
{
    "match_phrase_prefix" : {
        "brand" : "johnnie walker bl",
        "max_expansions": 50
    }
}
```

java equivalend code:

```
public static Query getMatchPrefixQuery(String fieldName, String fieldValue) {
		return new Query.Builder().matchPhrasePrefix(m -> m.field(fieldName).query(fieldValue).maxExpansions(1000)).build();
	}
```

** Term level Queries: ** Term-level queries find documents based on precise values in the structured data

i). ** Term Query: ** The term query is used to search for an exact term in a specific field. It matches documents that have an exact term in the specified field without any analysis or tokenization.

```
{
  "term": {
    "user": "johnsmith"
  }
}
```

java equivalend code:

```
public static Query getTermQuery(String fieldName, String fieldValue) {
		return new Query.Builder().term(t -> t.field(fieldName).value(fieldValue)).build();
	}
```

ii). **Terms Query:** The terms query allows you to search for documents that match any of the specified terms in a field. It is useful when you want to perform an OR operation on multiple values

```
{
  "terms": {
    "status": [
      "open",
      "pending"
    ]
  }
}
```

java equilvalent code:

```
	public static Query getTermsQueryBuilder(final String fieldName, final List<BigInteger> ids) {
		List<FieldValue> fieldValueList = new ArrayList<>(ids.size());
		for (BigInteger id : ids) {
			fieldValueList.add(FieldValue.of(id.toString()));
		}
		TermsQuery termQuery = TermsQuery.of(t -> t.field(fieldName).terms(tr -> tr.value(fieldValueList)));
		return new Query.Builder().bool(b -> b.must(m -> m.terms(termQuery))).build();
	}
```

iii). **Range Query:** The range query is used to search for documents within a specified range of values in a numeric or date field. It allows you to define inclusive or exclusive boundaries

```
{
  "range": {
    "price": {
      "gte": 10,
      "lte": 100
    }
  }
}
```
java equivalent code:

```
public static Query getRangeQuery(String fieldName, JsonData fromValue, JsonData toValue) {
		return new Query.Builder().range(r->r.field(fieldName).gte(fromValue).lte(toValue)).build();
	}
```

iv). ** Wildcard query: ** Returns documents that contain terms matching a wildcard pattern.A wildcard operator is a placeholder that matches one or more characters

```
{
  "query": {
    "wildcard": {
      "user_id": {
        "value": "*value*",
      }
    }
  }
}
```

java equivalent code:

```
public static Query getWildCardQuery(String fieldName, String fieldVale) {
		return new Query.Builder().wildcard(w -> w.field(fieldName).value("*" + fieldVale + "*").caseInsensitive(true)).build();
	}
```



# Obviously to learn more and for deep understanding you should follow : [elasctic search](https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html)

