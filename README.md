# Shopstack Security HMAC

Verify the authenticity of Shopify requests and redirects by validating the request against the included
[HMAC](https://shopify.dev/apps/auth/oauth#verification) code.

[![Build](https://github.com/shopstack-projects/shopstack-security-hmac/actions/workflows/build.yml/badge.svg?branch=main)](https://github.com/shopstack-projects/shopstack-security-hmac/actions/workflows/build.yml)

## Dependencies

This library uses the [SLF4J API Module](https://mvnrepository.com/artifact/org.slf4j/slf4j-api), and we haven't bundled
a SLF4J compatible implementation library so that we can keep our artifact size small. You should define a SLF4J
compatible logger implementation in your project.

## Usage

You can use the `HmacVerifier` class to authenticate Shopify requests, using the HMAC codes and message body.

```java
String sharedSecret = System.getenv("SHOPIFY_SHARED_SECRET");
String hmac = httpRequest.getHeader("X-Shopify-Hmac-Sha256");
String message = httpRequest.getBody();

boolean result = new HmacVerifier(sharedSecret).apply(hmac, message);

if (!result) {
    // Request message could not be authenticated.
}
```

## Use Cases

### Verify a HMAC

```java
String sharedSecret = System.getenv("SHOPIFY_SHARED_SECRET");
String hmac = httpRequest.getHeader("X-Shopify-Hmac-Sha256");

// 1. Use the message body for Shopify Webhook requests.
String message = httpRequest.getBody();

// 2. Construct a message from the request query parameters.
Map<String, String> queryParams = httpRequest.getQueryParameters();
String message = queryParams.keySet().stream()
    .map(key -> key + "=" + queryParams.get(key))
    .collect(joining("&"));

// You can verify the request using the HMAC and message.
boolean result = new HmacVerifier(sharedSecret).apply(hmac, message);

if (!result) {
    // Request message could not be authenticated.
}
```

#### Remove the HMAC query parameter.

In cases where the Shopify HTTP request provides a HMAC in a `hmac` query parameter instead of in a
`X-Shopify-Hmac-Sha256` header, you will need to exclude the `hmac` query parameter from the constructed message body
before you can verify the HMAC. This is documented [here](https://shopify.dev/apps/auth/oauth#remove-the-hmac).

### Generate a HMAC

You can also generate a HMAC code if you need to using the `HmacGenerator`.

```java
String sharedSecret = System.getenv("SHOPIFY_SHARED_SECRET");
String message = "Hello world".

String hmac = new HmacGenerator(sharedSecret).apply(message);
```

## License

Distributed under the MIT License. See `LICENSE` for more information.
