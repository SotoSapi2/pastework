package io.pastework.core.api.exception;

/**
 * The exception thrown when registry error occurs related. For example,
 * when the expected registry couldn't be found or when trying to create registry with conflicting resource key.
 *
 * @since 1.0.0
 */
public class RegistryException extends PasteworkException
{
    public RegistryException(String message)
    {
        super(message);
    }
}
