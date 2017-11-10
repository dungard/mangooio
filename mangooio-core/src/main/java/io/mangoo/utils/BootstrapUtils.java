package io.mangoo.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.io.Resources;
import com.google.inject.AbstractModule;
import com.google.inject.Module;

import io.mangoo.enums.Default;
import io.mangoo.enums.Jvm;
import io.mangoo.enums.Key;
import io.mangoo.enums.Mode;
import io.mangoo.enums.Required;
import io.mangoo.enums.RouteType;

/**
 *
 * @author svenkubiak
 *
 */
public final class BootstrapUtils {
    private static final Logger LOG = LogManager.getLogger(BootstrapUtils.class);

    private BootstrapUtils() {
    }

    /**
     * Retrieves a RouteType enum based on a given method string
     *
     * @param method The method to check
     * @return The RouteType enum or null if given method is undefined
     */
    public static RouteType getRouteType(String method) {
        Objects.requireNonNull(method, Required.METHOD.toString());
        
        switch (method.toUpperCase(Locale.ENGLISH)) {
        case "GET":
        case "POST":
        case "PUT":
        case "DELETE":
        case "PATCH":
        case "OPTIONS":
        case "HEAD":
            return RouteType.REQUEST;
        case "WSS":
            return RouteType.WEBSOCKET;
        case "SSE":
            return RouteType.SERVER_SENT_EVENT;
        case "FILE":
            return RouteType.RESOURCE_FILE;
        case "PATH":
            return RouteType.RESOURCE_PATH;
        default:
            return null;
        }
    }

    /**
     * Retrieves the current version of the framework from the version.properties file
     *
     * @return Current mangoo I/O version
     */
    public static String getVersion() {
        String version = Default.VERSION.toString();
        try (InputStream inputStream = Resources.getResource(Default.VERSION_PROPERTIES.toString()).openStream()) {
            final Properties properties = new Properties();
            properties.load(inputStream);
            version = String.valueOf(properties.get(Key.VERSION.toString()));
        } catch (final IOException e) {
            LOG.error("Failed to get application version", e);
        }

        return version;
    }

    /**
     * Retrieves the logo from the logo file and returns the string
     * 
     * @return The mangoo I/O logo string
     */
    public static String getLogo() {
        String logo = "";
        try (InputStream inputStream = Resources.getResource(Default.LOGO_FILE.toString()).openStream()) {
            logo = IOUtils.toString(inputStream, Default.ENCODING.toString());
        } catch (final IOException e) {
            LOG.error("Failed to get application logo", e);
        }

        return logo;
    }
    
    /**
     * Checks that a given package name ends with an .
     * 
     * @param packageName The package name to check
     * @return A valid package name
     */
    public static String getPackageName(String packageName) {
        Objects.requireNonNull(packageName, Required.PACKAGE_NAME.toString());

        if (!packageName.endsWith(".")) {
            return packageName + '.';
        }

        return packageName;
    }

    /**
     * @return The OS specific path to src/main/java
     */
    public static String getBaseDirectory() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(System.getProperty("user.dir"))
        .append(File.separator)
        .append("src")
        .append(File.separator)
        .append("main")
        .append(File.separator)
        .append("java");
        
        return buffer.toString();
    }
    
    public static Mode getMode() {
        Mode mode = Mode.PROD;
        final String applicationMode = System.getProperty(Jvm.APPLICATION_MODE.toString());
        if (StringUtils.isNotBlank(applicationMode)) {
            switch (applicationMode.toLowerCase(Locale.ENGLISH)) {
                case "dev"  : mode = Mode.DEV;
                break;
                case "test" : mode = Mode.TEST;
                break;
                default     : mode = Mode.PROD;
                break;
            }
        }

        return mode;
    }
    
    public static boolean methodExists(String controllerMethod, Class<?> controllerClass) {
        boolean exists = false;
        for (final Method method : controllerClass.getMethods()) {
            if (method.getName().equals(controllerMethod)) {
                exists = true;
                break;
            }
        }

        if (!exists) {
            LOG.error("Could not find controller method '" + controllerMethod + "' in controller class '" + controllerClass.getSimpleName() + "'");
        }

        return exists;
    }

    public static List<Module> getModules() {
        final List<Module> modules = new ArrayList<>();
        try {
            final Class<?> applicationModule = Class.forName(Default.MODULE_CLASS.toString());
            modules.add(new io.mangoo.core.Module());
            modules.add((AbstractModule) applicationModule.getConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
            LOG.error("Failed to load modules. Check that conf/Module.java exists in your application", e);
        }
        
        return modules;
    }
}