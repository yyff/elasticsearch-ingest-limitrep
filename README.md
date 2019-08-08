# Elasticsearch limitrep Ingest Processor

This processor can limit repeated documents in specific time interval.

## Supported Elasticsearch Version

| Version |
| --- |
| 6.5.4   |

## Setup

In order to install this plugin, you need to create a zip distribution first by running

```bash
./gradlew clean check
```

This will run tests and produce a zip file in `build/distributions`.

After building the zip file, you can install it like this

```bash
bin/plugin install file:///path/to/ingest-limitrep/build/distribution/ingest-limitrep-6.5.4.0.zip
```


## Usage

```
PUT _ingest/pipeline/limitrep-pipeline
{
  "description": "A pipeline to do whatever",
  "processors": [
    {
      "limitrep" : {
        "field" : "my_field",
      }
    }
  ]
}

PUT /my-index/my-type/1?pipeline=limitrep-pipeline
{
  "my_field" : "the content that should be filtered"
}

GET /my-index/my-type/1

# content is filtered, so you get no documents
```

## Configuration

| Parameter | Use | Required |
| --- | --- | --- |
| field   | Field name of where to read the content from | Yes |



