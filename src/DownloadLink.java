/**
 * Created by IntelliJ IDEA.
 * User: rbs
 * Date: 16.03.12
 * Time: 19:06
 * To change this template use File | Settings | File Templates.
 */
public class DownloadLink {
    private String href, fileName;

    public DownloadLink(String href, String fileName) {
        this.href = href;
        this.fileName = fileName;
    }

    public String getHref() {
        return href;
    }

    public String getFileName() {
        return fileName;
    }
}
