package net.sf.jabref.logic.net;

import net.sf.jabref.JabRefPreferences;

import java.util.Objects;

public class ProxyPreferences {

    private final Boolean useProxy;
    private final String hostname;
    private final String port;
    private final Boolean useAuthentication;
    private final String username;
    private final String password;

    public ProxyPreferences(Boolean useProxy, String hostname, String port, Boolean useAuthentication, String username,
                            String password) {
        this.useProxy = useProxy;
        this.hostname = hostname;
        this.port = port;
        this.useAuthentication = useAuthentication;
        this.username = username;
        this.password = password;
    }

    public static ProxyPreferences loadFromPreferences(JabRefPreferences preferences) {
        Boolean useProxy = preferences.getBoolean(JabRefPreferences.PROXY_USE);
        String hostname = preferences.get(JabRefPreferences.PROXY_HOSTNAME);
        String port = preferences.get(JabRefPreferences.PROXY_PORT);
        Boolean useAuthentication = preferences.getBoolean(JabRefPreferences.PROXY_USE_AUTHENTICATION);
        String username = preferences.get(JabRefPreferences.PROXY_USERNAME);
        String password = preferences.get(JabRefPreferences.PROXY_PASSWORD);
        return new ProxyPreferences(useProxy, hostname, port, useAuthentication, username, password);
    }

    public final Boolean isUseProxy() {
        return useProxy;
    }

    public final String getHostname() {
        return hostname;
    }

    public final String getPort() {
        return port;
    }

    public final Boolean isUseAuthentication() {
        return useAuthentication;
    }

    public final String getUsername() {
        return username;
    }

    public final String getPassword() {
        return password;
    }

    public void storeInPreferences(JabRefPreferences preferences) {
        preferences.putBoolean(JabRefPreferences.PROXY_USE, isUseProxy());
        preferences.put(JabRefPreferences.PROXY_HOSTNAME, getHostname());
        preferences.put(JabRefPreferences.PROXY_PORT, getPort());
        preferences.putBoolean(JabRefPreferences.PROXY_USE_AUTHENTICATION, isUseAuthentication());
        preferences.put(JabRefPreferences.PROXY_USERNAME, getUsername());
        preferences.put(JabRefPreferences.PROXY_PASSWORD, getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(hostname, password, port, useAuthentication, useProxy, username);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ProxyPreferences) {
            ProxyPreferences other = (ProxyPreferences) obj;
            if (hostname == null) {
                if (other.hostname != null) {
                    return false;
                }
            } else if (!hostname.equals(other.hostname)) {
                return false;
            }
            if (password == null) {
                if (other.password != null) {
                    return false;
                }
            } else if (!password.equals(other.password)) {
                return false;
            }
            if (port == null) {
                if (other.port != null) {
                    return false;
                }
            } else if (!port.equals(other.port)) {
                return false;
            }
            if (useAuthentication == null) {
                if (other.useAuthentication != null) {
                    return false;
                }
            } else if (!useAuthentication.equals(other.useAuthentication)) {
                return false;
            }
            if (useProxy == null) {
                if (other.useProxy != null) {
                    return false;
                }
            } else if (!useProxy.equals(other.useProxy)) {
                return false;
            }
            if (username == null) {
                if (other.username != null) {
                    return false;
                }
            } else if (!username.equals(other.username)) {
                return false;
            }
            return true;
        }
        return false;
    }
}
