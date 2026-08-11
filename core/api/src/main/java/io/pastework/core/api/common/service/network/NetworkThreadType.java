package io.pastework.core.api.common.service.network;

/**
 * Specifies the type of thread context for handling network operations.
 */
public enum NetworkThreadType
{
    /**
     * Indicates that the operation handles on the dedicated network thread.
     */
    NETWORK,

    /**
     * Indicates that the operation handles on the main thread.
     */
    MAIN
}
