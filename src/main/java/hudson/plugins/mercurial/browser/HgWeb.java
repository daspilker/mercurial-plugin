package hudson.plugins.mercurial.browser;

import hudson.Extension;
import hudson.plugins.mercurial.MercurialChangeSet;
import hudson.util.FormValidation;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

/**
 * Mercurial web interface served using the standalone server
 * or hgweb CGI scripts.
 */
public class HgWeb extends HgBrowser {
    
    @DataBoundConstructor
    public HgWeb(String url) throws MalformedURLException {
        super(url);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public URL getChangeSetLink(MercurialChangeSet changeSet)
            throws IOException {
        current = changeSet;
        // TODO: consider verifying the repository connection to tip at configuration time?
        return new URL(getUrl(), "rev/" + changeSet.getShortNode());
    }

    /**
     * {@inheritDoc}
     * 
     * Throws {@link IllegalStateException} when this method is called before at least one call 
     * to {@literal getChangeSetLink(MercurialChangeSet)}.
     */
    @Override
    public URL getFileLink(String path) throws MalformedURLException {
        checkCurrentIsNotNull();
        // http://hg.friedenhagen.net/index.cgi/mercurial-hudson/file/d736d15e5389/src/main/java/hudson/plugins/mercurial/browser/HgBrowser.java
        return new URL(getUrl(), "file/" + current.getShortNode() + "/" + path);
    }
    
    /**
     * {@inheritDoc}
     * 
     * Throws {@link IllegalStateException} when this method is called before at least one call 
     * to {@literal getChangeSetLink(MercurialChangeSet)}.
     */
    @Override
    public URL getDiffLink(String path) throws MalformedURLException {
        checkCurrentIsNotNull();
        // http://hg.friedenhagen.net/index.cgi/mercurial-hudson/diff/d736d15e5389/src/main/java/hudson/plugins/mercurial/browser/HgBrowser.java
        return new URL(getUrl(), "diff/" + current.getShortNode() + "/" + path);
    }

    @Extension
    public static class DescriptorImpl extends HgBrowserDescriptor {
        public String getDisplayName() {
            return "hgweb";
        }

        @Override public FormValidation doCheckUrl(@QueryParameter String url) {
            return _doCheckUrl(url);
        }

    }

    private static final long serialVersionUID = 1L;
}
