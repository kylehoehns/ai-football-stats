# Spring AI Example

This is a Spring Boot web application that uses [Spring AI](https://docs.spring.io/spring-ai/reference/) to talk with OpenAi's GPT-3.5 model. It utilizes [retrieval augmented generation](https://ai.meta.com/blog/retrieval-augmented-generation-streamlining-the-creation-of-intelligent-natural-language-processing-models/) to answer questions about the [Iowa Hawkeyes football team for the 2023-24 season](https://hawkeyesports.com/sports/football/cumestats/season/2023-24/).

## Setup

* Create an OpenAI account and [get an API key](https://platform.openai.com/api-keys).
* Set up [billing settings](https://platform.openai.com/account/billing/overview) for your account with a small spending limit. 
* Set an environment variable named `SPRING_AI_OPENAI_API_KEY` to your OpenAI API key.
* Install Java 21 or later.
* Run `./gradlew bootRun` to start the application.

## Populating Vector Storage
* Once the application is running, you can populate the vector storage by running `curl -X POST http://localhost:8080/ai/create-embeddings`.
* This will create embeddings for all the data in `src/main/resources/data` and store them in the vector storage file called `iowa-football-vector-storage.json` in your project's `target/classes/vector-store` directory, so you only have to generate them once.

## Using the API
* Once the vector storage is populated, you can use the API to get predictions from GPT-3.5 using the embeddings in the vector storage.
* Run the following to get a response about the number of passing touchdowns.

```shell
curl --request POST \
  --url 'http://localhost:8080/ai' \
  --header 'Content-Type: application/json' \
  --data '{
	"message": "How many passing touchdowns were there?"
}'
```

You'll receive a response like this:
```json
{
  "text": "There were 9 passing touchdowns for the Iowa Hawkeyes.",
  "info": {
    "role": "assistant"
  },
  "choiceMetadata": {
    "contentFilterMetadata": null,
    "finishReason": "stop"
  }
}
```