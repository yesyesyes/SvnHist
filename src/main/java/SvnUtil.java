//import org.tmatesoft.svn.core.SVNException;
//import org.tmatesoft.svn.core.SVNLogEntry;
//import org.tmatesoft.svn.core.SVNNodeKind;
//import org.tmatesoft.svn.core.SVNURL;
//import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
//import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
//import org.tmatesoft.svn.core.io.SVNRepository;
//import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
//import org.tmatesoft.svn.core.wc.SVNWCUtil;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.util.*;
//
//public class SvnUtil {
//
//    private String repositoryUrl;
//    private String repositoryRoot;
//    private String name;
//    private String password;
//    private String userStory;
//    private static final String fileName = "config.properties";
//
//    public void writeChangedFilesTree() {
//        System.out.println("Retrieving commit history...");
//        List<String> list = getChangedPaths();
//        if(list.isEmpty()) {
//            System.out.println("No changes");
//        } else {
//        for(String s : list) {
//            System.out.println(s);
//        }
//        Formatter.format(list, userStory);
//        }
//    }
//
//    public static void main(String[] args) {
//        new SvnUtil().writeChangedFilesTree();
//    }
//
//    public SvnUtil() {
//        Properties prop = new Properties();
//        try {
//            prop.load(new FileInputStream(new File(fileName)));
//        }
//        catch (Exception e) {
//            System.out.println("Can't load config.properties");
//            e.printStackTrace();
//        }
//        repositoryUrl = prop.getProperty("repositoryUrl");
//        repositoryRoot = prop.getProperty("repositoryRoot");
//        if(repositoryUrl == null) {
//            repositoryUrl = repositoryRoot;
//        }
//        name = prop.getProperty("name");
//        password = prop.getProperty("password");
//        userStory = prop.getProperty("userStory");
//        if(repositoryUrl == null || name == null || password == null || userStory == null) {
//            System.out.println("repositoryUrl, name, password and userStory must be specified in config.properties file");
//        }
//    }
//
//    @SuppressWarnings({ "rawtypes", "unchecked" })
//    private List<String> getChangedPaths() {
//        List<String> paths = new LinkedList<String>();
//        Collection logEntries;
//
//        try {
//            logEntries = getLogs(0, -1);
//            for (Iterator entries = logEntries.iterator(); entries.hasNext();) {
//                final SVNLogEntry logEntry = (SVNLogEntry) entries.next();
//
//                if (!logEntry.getMessage().contains(userStory)) {
//                    continue;
//                }
//                if (logEntry.getChangedPaths().size() > 0) {
//                    Set changedPathsSet = logEntry.getChangedPaths().keySet();
//                    paths.addAll(changedPathsSet);
//                    for(Object e: changedPathsSet) {
//                        System.out.println(e);
//                    }
//                }
//            }
//            paths = deleteUnexisted(new TreeSet<String>(paths));
//            Collections.sort(paths);
//        }
//        catch (SVNException e) {
//            e.printStackTrace();
//        }
//        return paths;
//    }
//
//    @SuppressWarnings("rawtypes")
//    private List<String> deleteUnexisted(Set<String> paths) throws SVNException {
//        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(
//                name, password);
//        SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(repositoryRoot));
//        repository.setAuthenticationManager(authManager);
//
//        for (Iterator i = paths.iterator(); i.hasNext();) {
//            String path = (String) i.next();
//            SVNNodeKind nodeKind = repository.checkPath(path, -1);
//            if (nodeKind == SVNNodeKind.NONE)
//                i.remove();
//        }
//        return new LinkedList<String>(paths);
//    }
//
//    @SuppressWarnings("rawtypes")
//    private Collection getLogs(long startRevision, long endRevision)
//            throws SVNException {
//        DAVRepositoryFactory.setup();
//        SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(repositoryUrl));
//        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(
//                name, password);
//        repository.setAuthenticationManager(authManager);
//
//        Collection logEntries = repository.log(new String[] { "" }, null,
//                startRevision, endRevision, true, true);
//        return logEntries;
//    }
//
//}
