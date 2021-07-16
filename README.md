# Shopstack Security HMAC

Verify the authenticity of Shopify requests and redirects by validating the request against the included
[HMAC](https://shopify.dev/apps/auth/oauth#verification) code.

[![Build](https://github.com/alanguerin/shopstack-security-hmac/actions/workflows/build.yml/badge.svg?branch=main)](https://github.com/alanguerin/shopstack-security-hmac/actions/workflows/build.yml)

## Dependencies

This library uses the SLF4J API, but we haven't bundled an implementation library so that we can keep our artifact size
small. You will need to define a compatible logger implementation in your project.

## Usage

You can use the `HmacVerifier` class to authenticate Shopify requests, using the HMAC codes and message body.

    String sharedSecret = System.getenv("SHOPIFY_SHARED_SECRET");
    String hmac = httpRequest.getHeader("X-Shopify-Hmac-Sha256");
    String message = httpRequest.getBody();

    boolean result = new HmacVerifier(sharedSecret).apply(shopifyHmac, message);
    
    if (!result) {
        // Request message could not be authenticated.
    }

## Use Cases

    String sharedSecret = System.getenv("SHOPIFY_SHARED_SECRET");
    String hmac = httpRequest.getHeader("X-Shopify-Hmac-Sha256");
    
    // Use the message body for Shopify Webhook requests.
    String message = httpRequest.getBody();

    // Or, construct a message from the request query parameters if authenticating a Shopify HTTP GET request. 
    Map<String, String> queryParams = httpRequest.getQueryParameters();
    String message = queryParams.keySet().stream()
        .map(key -> key + "=" + queryParams.get(key))
        .collect(joining("&"));

    // You can verify the request with the HMAC and message.
    boolean result = new HmacVerifier(sharedSecret).apply(shopifyHmac, message);
    
    if (!result) {
        // Request message could not be authenticated.
    }
    
This library also allows you to generate a HMAC code if you need to.

    String sharedSecret = System.getenv("SHOPIFY_SHARED_SECRET");
    String message = "Hello world".

    String hmac = new HmacGenerator(sharedSecret).apply(message);

## License

Distributed under the MIT License. See `LICENSE` for more information.
