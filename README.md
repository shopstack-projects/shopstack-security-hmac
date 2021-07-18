# Shopstack Security HMAC

Authenticate Shopify HTTP [requests](https://shopify.dev/apps/auth/oauth#verification) and
[webhooks](https://shopify.dev/apps/webhooks#6-verify-a-webhook), by validating the message against the provided HMAC
code. 

[![Build](https://github.com/shopstack-projects/shopstack-security-hmac/actions/workflows/build.yml/badge.svg?branch=main)](https://github.com/shopstack-projects/shopstack-security-hmac/actions/workflows/build.yml?query=branch%3Amain)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/4b15e3f1c123432eb7d274bcc083b199)](https://www.codacy.com/gh/shopstack-projects/shopstack-security-hmac/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=shopstack-projects/shopstack-security-hmac&amp;utm_campaign=Badge_Grade)

## Getting Started

_Maven configuration coming soon._

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

The message to use for comparison will typically consist of either the HTTP request body (for POST requests), or the
request's query parameters (for GET requests).

#### Scenario 1 - Using HTTP Body

```java
String sharedSecret = System.getenv("SHOPIFY_SHARED_SECRET");
String hmac = httpRequest.getHeader("X-Shopify-Hmac-Sha256");

String message = httpRequest.getBody();

// Verify the request using the HMAC and message.
boolean result = new HmacVerifier(sharedSecret).apply(hmac, message);

if (!result) {
    // Shopify message could not be verified.
}
```

#### Scenario 2 - Using HTTP Query Parameters

When using query parameters, be sure to first [remove the `hmac` query parameter](https://shopify.dev/apps/auth/oauth#remove-the-hmac).

```java
String sharedSecret = System.getenv("SHOPIFY_SHARED_SECRET");
String hmac = httpRequest.getHeader("X-Shopify-Hmac-Sha256");

Map<String, String> queryParams = httpRequest.getQueryParameters();

String message = queryParams.keySet().stream()
    .filter(key -> !key.equalsIgnoreCase("hmac"))
    .map(key -> key + "=" + queryParams.get(key))
    .collect(joining("&"));

// Verify the request using the HMAC and message.
boolean result = new HmacVerifier(sharedSecret).apply(hmac, message);

if (!result) {
    // Shopify message could not be verified.
}
```

### Generate a HMAC

You can also generate a HMAC code if you need to using the `HmacGenerator`.

```java
String sharedSecret = System.getenv("SHOPIFY_SHARED_SECRET");
String message = "Hello world".

String hmac = new HmacGenerator(sharedSecret).apply(message);
```

## Building from Source

You don't need to build from source, but if you want to try it out, you can use the included Gradle wrapper.
You will also need JDK 11.

```shell
$ ./gradlew clean test build --info
```

## License

Distributed under the MIT License. See `LICENSE` for more information.
