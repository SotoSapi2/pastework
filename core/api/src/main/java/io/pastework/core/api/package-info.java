/**
 * The Core API provides a central point for accessing service implementation interface represented
 * as {@link io.pastework.spi.IPasteworkService}.
 * <p>
 * This module provides common modding utility and abstraction for most mod loader API provides, such as
 * game content registry, networking, event, etc.
 * <p>
 * Implementations of this module are also responsible for invoking framework dependant entrypoints:
 * {@link io.pastework.spi.ICommonEntrypoint} and {@link io.pastework.spi.IClientEntrypoint}.
 * <p>
 * Other modders can implement their own service through provided SPI module.
 * For more information on creating extension see, {@link io.pastework.spi}.
 *
 * @since 1.0.0
 */
package io.pastework.core.api;