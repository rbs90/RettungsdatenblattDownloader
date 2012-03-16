import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.NodeVisitor;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: rbs
 * Date: 16.03.12
 * Time: 21:13
 * To change this template use File | Settings | File Templates.
 */
public class PeugeotLinkFetcher {

    private ArrayList<DownloadLink> links = new ArrayList<DownloadLink>();

    public PeugeotLinkFetcher(String baseURL) throws ParserException, IOException {

        NodeVisitor visitor = new NodeVisitor(true) {

            private Boolean dataField = false;
            private String currentCat = "";


            @Override
            public void visitTag(Tag tag) {

                String tagName = tag.getTagName();

                if(dataField){
                    if (tagName.equalsIgnoreCase("a")){
                        String link = tag.getAttribute("href");
                        String name = tag.getChildren().elementAt(0).getText();
                        if (link != null && !link.startsWith("http://"))
                        {
                            if (!link.startsWith("/")) link = "/" + link.trim();
                            link = "http://services.peugeot.de/" + link;
                        }
                        links.add(new DownloadLink(link, "Peugeot" + File.separator
                                + name.replaceAll("/", "_").trim() + ".pdf"));

                    }
                }
                else{
                    if(tagName.equalsIgnoreCase("div")) {
                        String classAt = tag.getAttribute("class");
                        if (classAt != null && classAt.equals("html")){
                            System.out.println("REACHED data field...");
                            dataField = true;
                        }
                    }
                }
            }

            @Override
            public void visitEndTag(Tag tag){
                if (tag.getTagName().equalsIgnoreCase("div") && dataField){
                    dataField = false;
                    System.out.println("EXIT DATA");
                }
            }
        };

        URL url = new URL(baseURL);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; de-DE; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");

        Parser parser = new Parser(urlConnection);
        parser.visitAllNodesWith(visitor);

    }

    public ArrayList<DownloadLink> getLinks() {
        return links;
    }
}
