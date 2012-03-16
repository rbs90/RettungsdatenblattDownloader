import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Created by IntelliJ IDEA.
 * User: rbs
 * Date: 16.03.12
 * Time: 19:28
 * To change this template use File | Settings | File Templates.
 */
public class URLDownloader{

    public static Boolean loadToFile(String url, String file){

        ReadableByteChannel rbc = null;
        FileOutputStream fos = null;
        try {
            rbc = Channels.newChannel(new URL(url).openStream());
            File create = new File(file);
            if(!create.exists()){
                create.getParentFile().mkdirs();
                create.createNewFile();
            }
            fos = new FileOutputStream(file);
            fos.getChannel().transferFrom(rbc, 0, 1 << 24);
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return false;
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return false;
        }

        return true;
    }

    public static Boolean loadToFile(DownloadLink link){
        return loadToFile(link.getHref(), link.getFileName());
    }
}
