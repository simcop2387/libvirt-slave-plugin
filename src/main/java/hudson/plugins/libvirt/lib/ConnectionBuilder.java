package hudson.plugins.libvirt.lib;

import com.cloudbees.plugins.credentials.common.StandardUsernameCredentials;
import com.cloudbees.plugins.credentials.common.StandardUsernamePasswordCredentials;
import hudson.plugins.libvirt.lib.libvirt.LibVirtConnectImpl;

/**
 * Created by magnayn on 05/02/2014.
 */
public class ConnectionBuilder {

    private String uri;
    private boolean readOnly = false;

    private String hypervisorType;
    private String hypervisorHost;
    private int    hypervisorPort = 22;
    private String hypervisorSysUrl;

    private String protocol = "ssh://";
    private StandardUsernameCredentials credentials;

    public static ConnectionBuilder newBuilder() { return new ConnectionBuilder(); }

    public ConnectionBuilder hypervisorType(String hypervisorType) {
        this.hypervisorType = hypervisorType;
        return this;
    }

    public ConnectionBuilder hypervisorHost(String hypervisorHost) {
        this.hypervisorHost = hypervisorHost;
        return this;
    }

    public ConnectionBuilder hypervisorPort(int hypervisorPort) {
        this.hypervisorPort = hypervisorPort;
        return this;
    }

    public ConnectionBuilder hypervisorSysUrl(String hypervisorSysUrl) {
        this.hypervisorSysUrl = hypervisorSysUrl;
        return this;
    }

    public ConnectionBuilder protocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    public ConnectionBuilder withCredentials(StandardUsernameCredentials standardUsernameCredentials) {
        this.credentials = standardUsernameCredentials;
        return this;
    }

    public ConnectionBuilder useUri(String uri) {
        this.uri = uri;
        return this;
    }

    public ConnectionBuilder readOnly() {
        this.readOnly = true;
        return this;
    }

    public IConnect build() throws VirtException {
        if( uri == null )
            uri = constructHypervisorURI();
        return new LibVirtConnectImpl(uri, readOnly);
    }

    public String constructHypervisorURI () {
        // Fixing JENKINS-14617
        final String separator = (hypervisorSysUrl.contains("?")) ? "&" : "?";
        return hypervisorType.toLowerCase() + "+" + protocol + this.credentials.getUsername() + "@" + hypervisorHost + ":" + hypervisorPort + "/" + hypervisorSysUrl + separator + "no_tty=1";
    }
}
