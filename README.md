# Shopstack Security HMAC

Authenticate Shopify HTTP [requests](https://shopify.dev/apps/auth/oauth#verification) and
[webhooks](https://shopify.dev/apps/webhooks#6-verify-a-webhook), by validating the message against the provided HMAC
code. 

[![Build](https://github.com/shopstack-projects/shopstack-security-hmac/actions/workflows/build.yml/badge.svg?branch=main)](https://github.com/shopstack-projects/shopstack-security-hmac/actions/workflows/build.yml?query=branch%3Amain)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/4b15e3f1c123432eb7d274bcc083b199)](https://www.codacy.com/gh/shopstack-projects/shopstack-security-hmac/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=shopstack-projects/shopstack-security-hmac&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://app.codacy.com/project/badge/Coverage/4b15e3f1c123432eb7d274bcc083b199)](https://www.codacy.com/gh/shopstack-projects/shopstack-security-hmac/dashboard?utm_source=github.com&utm_medium=referral&utm_content=shopstack-projects/shopstack-security-hmac&utm_campaign=Badge_Coverage)

## Getting Started

Project artifacts can be downloaded from the [Maven Central Repository](https://search.maven.org/artifact/dev.shopstack.security/shopstack-security-hmac).

```xml
<dependency>
    <groupId>dev.shopstack.security</groupId>
    <artifactId>shopstack-security-hmac</artifactId>
    <version>1.0.0.RELEASE</version>
</dependency>
```

### SLF4J

This library uses the SLF4J API module without an implementation library, to keep our artifact size small.
You should include an SLF4J compatible logger implementation in your project.

```xml
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
</dependency>
```

## Usage

### Verify a HMAC

Use the `HmacVerifier` to authenticate a Shopify request by evaluating the provided HMAC code against the message body.

Shopify HTTP requests and redirects may include the HMAC code in either a `X-Shopify-Hmac-Sha256` HTTP header or in a
`hmac` query parameter.

The message to use when generating a comparison HMAC will either consist of the HTTP request body (for POST requests),
or the request's query parameters (for GET requests).

#### Scenario 1 - Using the HTTP Body

```java
String secret = System.getenv("SHOPIFY_SHARED_SECRET");
String hmac = httpRequest.getHeader("X-Shopify-Hmac-Sha256");

String message = httpRequest.getBody();

// Verify the request using the HMAC and message.
boolean result = new HmacVerifier(secret).apply(hmac, message);

if (!result) {
    // Shopify message could not be verified.
}
```

#### Scenario 2 - Using HTTP Query Parameters

When using query parameters, be sure to first [remove the `hmac` query parameter](https://shopify.dev/apps/auth/oauth#remove-the-hmac).

```java
String secret = System.getenv("SHOPIFY_SHARED_SECRET");

Map<String, String> queryParameters = httpRequest.getQueryParameters();

String hmac = queryParameters.get("hmac");
    
String message = queryParameters.keySet().stream()
    .filter(key -> !key.equalsIgnoreCase("hmac"))
    .map(key -> key + "=" + queryParameters.get(key))
    .sorted() // Lexicographic order is required.
    .collect(joining("&"));

// Verify the request using the HMAC and message.
boolean result = new HmacVerifier(secret).apply(hmac, message);

if (!result) {
    // Shopify message could not be verified.
}
```

#### Scenario 3 - Using a Base16 (Hexadecimal) Encoding

Shopify encodes the HMAC value in Base16 (Hexadecimal) for requests used when granting permissions to an application.
You can configure the data encoding when creating the `HmacVerifier`.

We default to using a Base64 encoding otherwise, which is suitable for other Shopify requests.

```java
String secret = System.getenv("SHOPIFY_SHARED_SECRET");

Map<String, String> queryParameters = httpRequest.getQueryParameters();

String hmac = queryParameters.get("hmac");

String message = queryParameters.keySet().stream()
    .filter(key -> !key.equalsIgnoreCase("hmac"))
    .map(key -> key + "=" + queryParameters.get(key))
    .sorted() // Lexicographic order is required.
    .collect(joining("&"));

// Verify the request using the HMAC and message.
boolean result = new HmacVerifier(secret, Encoding.BASE16) // Specify a Base16 encoding as required.
    .apply(hmac, message);

if (!result) {
    // Shopify message could not be verified.
}
```

### Generate a HMAC

You can also generate a HMAC code directly using the `HmacGenerator`.

```java
String secret = System.getenv("SHOPIFY_SHARED_SECRET");
String message = "Hello world";

String hmac = new HmacGenerator(secret).apply(message);
```

## Building from Source

You don't need to build from source, but if you want to try it out, you can use the included Gradle wrapper.
You will also need JDK 11.

```shell
$ ./gradlew clean test build --info
```

## License

Distributed under the MIT License. See `LICENSE` for more information.
