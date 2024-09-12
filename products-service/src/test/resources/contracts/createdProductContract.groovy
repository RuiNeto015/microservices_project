package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("""
Sends a created event product

```
given:
	product is valid
when:
	the admin created a product
then:
	the system it will be notified of the event
```

""")
    input {
        triggeredBy("createProduct()")
    }
    label("triggerProductCreatedEvent")
    outputMessage {
        sentTo("ProductCreated")
        body([
                messageId: anyUuid(),
                timestamp: anyDateTime(),
                topic    : anyNonBlankString(),
                sender   : anyUuid().toString(),
                product  : [
                        sku         : anyNonBlankString(),
                        designation : anyNonBlankString(),
                        description : anyNonBlankString(),
                        numApprovals: anyInteger()
                ]
        ])
    }
}