/**
 * This package provides interface and service for content registry operations that's handled by them game natively.
 * For example, registering item, block and entity.
 * <p>
 * All registration operation including, creating registrars, enqueueing new entries, and managing registrars or
 * registries must be done during the mod initialization phase when the framework calls mod entrypoint. Any modification
 * enqueueing attempts after the registration is finalized/processed will result in
 * {@link io.pastework.core.api.exception.RegistryException} being thrown.
 * <p>
 * Bear in mind, actual registration under the hood can be deferred (in Forge/NeoForge platform) or
 * registered instantly (in Fabric platform). To avoid unexpected error, always prefer
 * registration queue mechanisms provided by this service rather than registering early directly.
 * <p>
 * Example of enqueuing entries:
 * <pre>
 *  {@code
 *  private static final ICommonRegistry REGISTRY_SERVICE = ICommonRegistry.getService();
 *  private static final ICommonRegistrar<Item> REGISTRAR = REGISTRY_SERVICE.createRegistrar(
 *      HelloWorldMod.MOD_ID, // the namespace
 *      Registries.ITEM // the registry type
 *  );
 *
 *  public static IEntryHolder<Item> RUBY = REGISTRAR.register(
 *      "ruby",
 *      Item::new
 *  );
 *
 *  public static void initialize()
 *  {
 *      REGISTRY_SERVICE.enqueueRegistrar(REGISTRAR);
 *  }
 *  }
 * </pre>
 * <p>
 * Example of enqueuing new registry:
 * <pre>
 *  {@code
 *  private static final ICommonRegistry REGISTRY_SERVICE = ICommonRegistry.getService();
 *  private static final ResourceKey<Registry<CustomType>> CUSTOM_REGISTRY_KEY = ResourceKey.createRegistryKey(
 *      Identifier.of(HelloWorldMod.MOD_ID, "custom_registry")
 *  );
 *
 *  public static void initialize()
 *  {
 *      REGISTRY_SERVICE.enqueueNewRegistry(CUSTOM_REGISTRY_KEY);
 *  }
 *  }
 * </pre>
 *
 * @since 1.0.0
 *
 * @see io.pastework.core.api.common.service.registry.ICommonRegistry
 * @see io.pastework.spi.ICommonEntrypoint
 * @see io.pastework.spi.IClientEntrypoint
 */
package io.pastework.core.api.common.service.registry;