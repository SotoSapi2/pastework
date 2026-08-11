# Architecture Design
<small>
I'm sorry, this doc is unfinished.
</small>

Pastework is built using layered architecture design to decouple the public API from loader-specific implementations.

Pastework design enforces the splitting of clients and common sources for internal and consumer uses to 
guarantee classes aren't leaking from their expected environment which might cause problem.

## Modules
Pastework currently provides one main module "core", which provides fundamental services most 
loader APIs usually provided natively (e.g., Registry, Events, Networking, etc.) and handles consumer 
entrypoint. And "spi" module to provides framework extensions.

Pastework is planned to provide modules to abstract third-party libraries that are widely used to 
create content, like curio and energy, the loader APIs aren't capable to.

### Modules Layers

Each module contains 3 layers:

#### 1. API Layer
The interface layer for consumers to use. It defines the abstraction of each service and doesn't contain any 
implementation code. this layer acts as an interface to interact with the service implementation at compile time 
for the consumer, and they should declare this layer as compile only.

#### 2. Base Layer
This layer reduces the code duplication by handling platform-independent tasks. 
It provides abstract implementations for the API layer to be extended by platform layers. 
This layer is only intended for internal usage, and consumers should not use it directly.

#### 3. Platform Layers
These layers contain the actual glue code. 
They implement the service interfaces using loader-specific APIs 
(e.g., wrapping Fabric's Registry or NeoForge's Event Bus) and provide the necessary Mixins. 
Consumers should declare this layer as runtime only.

### Artifact Bundling & Distribution
Every module layer bundles the artifacts of the layers above them. 
The module buildscript is responsible for bundling artifacts from all of its platform layers to provide runtime of the 
library. This runtime artifact is what should be published to mod distributors (Modrinth, CurseForge, etc.) 
for the end users (players) to use.