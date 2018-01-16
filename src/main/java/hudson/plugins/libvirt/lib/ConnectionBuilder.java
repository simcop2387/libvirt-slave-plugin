package hudson.plugins.libvirt.lib;

import com.cloudbees.plugins.credentials.common.StandardUsernameCredentials;
import com.cloudbees.plugins.credentials.common.StandardUsernamePasswordCredentials;
import hudson.plugins.libvirt.lib.libvirt.LibVirtConnectImpl;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Created by magnayn on 05/02/2014.
 */
public class ConnectionBuilder {
    private static final Logger LOGGER = Logger.getLogger(ConnectionBuilder.class.getName());
    private boolean useNativeJava = false;
    private String uri;
    private boolean readOnly = false;

    private String hypervisorType;
    private String hypervisorHost;
    private int    hypervisorPort = 22;
    private String hypervisorSysUrl;

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
        return hypervisorType.toLowerCase() + "+ssh://" + this.credentials.getUsername() + "@" + hypervisorHost + ":" + hypervisorPort + "/" + hypervisorSysUrl + separator + "no_tty=1";
    }

    public String constructNativeHypervisorURI () {
	String Url;
	// Fixing JENKINS-14617
	Url = hypervisorType.toLowerCase() + ":///" + hypervisorSysUrl;

	LogRecord rec = new LogRecord(Level.INFO, "nativeHypervisor: {0}");
	rec.setParameters(new Object[]{Url});
	LOGGER.log(rec);

	return Url;
    }
}
