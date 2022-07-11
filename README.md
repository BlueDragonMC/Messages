# Messages
## Installation
Publish the artifact to your local maven repository:
```shell
./gradlew build publishToMavenLocal
```
Use it in a Gradle build file:
```kotlin
dependencies {
    implementation("com.bluedragonmc:messages:$version")
}
```
## Usage
```kotlin
// Create a client to connect to RabbitMQ
val client = AMQPClient(hostname = "rabbitmq", port = 5672, polymorphicModuleBuilder = polymorphicModuleBuilder)
```
You must use the `polymorphicModuleBuilder` found in `Messages.kt` in the constructor of your `AMQPClient`. This is because all subclasses (in an open polymorphic setup like this one) must be registered in `kotlinx.serialization` for security reasons.