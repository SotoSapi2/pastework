# Pastework
A framework to unify Minecraft cross-loader/platform mod development. Currently still in development.

## Setting up
*TODO*

---

## State of the project
At the moment, this framework only supports Minecraft 1.21.11 for both NeoForge and Fabric.
There are no plans to backport Pastework for previous Minecraft version,
although support for more loaders will be implemented in the future.

Keep in mind that the currently available implementation (especially events) has not been fully tested yet.
They may behave differently with different loaders or simply work unexpectedly.
Please report any issues.

Most of the API declaration is undocumented. You might have hard
time understanding them.

If you have any questions, feel free to open an issue or DM me on Discord.
(@soto_sapi1, I'm in both Fabric and NeoForge servers.)

---

## Why does this framework exist?
Most of the abstraction libraries I found, in my opinion, aren't
sophisticated enough and painful to work with. Because of this, I want to make my own library.
Based on my own design and my needs.

The main use case of this framework is to make it easier to
develop cross-loader mods. This framework provides a unified API for common
modding tasks such as registry, events, networking, etc.

---

## TODO Stuff
### Legends
- ✅ Complete
- 🛠️ Work in progress
- ❌ No progress have been made

### Loader Support
- ✅ Fabric
- ✅ NeoForge
- ❌ Forge
- ❌ Quilt

### Core API
- ✅ Service provider
- ✅ Framework managed entrypoint
- ✅ Common registry service
- 🛠️ Events
- ✅ Networking (NOT FULLY TESTED)
    - ✅ Play channel
    - ✅ Configuration channel
- ✅ Keybind registry
- ✅ Data attachment
- ✅ UI service
- ❌ Config service

### Extension API
- ❌ Capabilities
- ❌ Curio

### Dev Environment
- ❌ Template project
- ❌ Proper documentation
- ❌ Automated testing support
- ❌ Datagen support
- 🛠️ Gradle plugin
    -  🛠️ Loom and ModDevGradle abstraction
    -  🛠️ Metadata/Mod Files Generator