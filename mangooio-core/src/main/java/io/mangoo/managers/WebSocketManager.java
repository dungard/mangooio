package io.mangoo.managers;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.mangoo.cache.Cache;
import io.mangoo.enums.CacheName;
import io.mangoo.enums.Default;
import io.mangoo.enums.Required;
import io.mangoo.helpers.RequestHelper;
import io.mangoo.providers.CacheProvider;
import io.undertow.websockets.core.WebSocketChannel;

/**
 *
 * @author svenkubiak
 * @deprecated  As of release 4.6.0, replaced by io.mangoo.services.WebSocketService
 *              Will be removed in 5.0.0
 */
@Deprecated
@Singleton
public class WebSocketManager {
    private final Cache cache;
    private final RequestHelper requestHelper;
    
    @Inject
    private WebSocketManager(CacheProvider cacheProvider,  RequestHelper requestHelper) {
        Objects.requireNonNull(cacheProvider, Required.CACHE_PROVIDER.toString());
        
        this.cache = cacheProvider.getCache(CacheName.WSS);
        this.requestHelper = requestHelper;
    }

    /**
     * Adds a new channel to the manager
     *
     * @param channel channel The channel to put
     */
    @SuppressWarnings("all")
    public void addChannel(WebSocketChannel channel) {
        Objects.requireNonNull(channel, Required.CHANNEL.toString());

        final String url = this.requestHelper.getWebSocketURL(channel);
        Set<WebSocketChannel> channels = getChannels(url);
        if (channels == null) {
            channels = new HashSet<>();
            channels.add(channel);
        } else {
            channels.add(channel);
        }
        setChannels(url, channels);
    }

    /**
     * Sets the URI resources for a given URL
     *
     * @param uri The URI resource for the connection
     * @param channels The channels for the URI resource
     */
    public void setChannels(String uri, Set<WebSocketChannel> channels) {
        Objects.requireNonNull(uri, Required.URI.toString());
        Objects.requireNonNull(channels, Required.URI_CONNECTIONS.toString());

        this.cache.put(Default.WSS_CACHE_PREFIX.toString() + uri, channels);
    }

    /**
     * Retrieves all channels under a given URL
     *
     * @param uri The URI resource for the channels
     *
     * @return A Set of channels for the URI resource
     */
    public Set<WebSocketChannel> getChannels(String uri) {
        Objects.requireNonNull(uri, Required.URI.toString());

        final Set<WebSocketChannel> channels = this.cache.get(Default.WSS_CACHE_PREFIX.toString() + uri);

        return (channels == null) ? new HashSet<>() : channels;
    }

    /**
     * Removes all URI resources for a given URL
     *
     * @param uri The URI resource for the connection
     */
    public void removeChannels(String uri) {
        Objects.requireNonNull(uri, Required.URI.toString());

        this.cache.remove(Default.WSS_CACHE_PREFIX.toString() + uri);
    }

    /**
     * Closes all connections for a given URI resource
     *
     * @param uri The URI resource for the connection
     */
    public void close(String uri) {
        Objects.requireNonNull(uri, Required.URI.toString());

        final Set<WebSocketChannel> channels = getChannels(uri);
        if (channels != null) {
            channels.forEach((WebSocketChannel channel) -> {
                if (channel.isOpen()) {
                    IOUtils.closeQuietly(channel);
                }
            });
            removeChannels(uri);
        }
    }
}