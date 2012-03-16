import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.NodeVisitor;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: rbs
 * Date: 16.03.12
 * Time: 09:51
 * To change this template use File | Settings | File Templates.
 */
public class BMWLinkFetcher {

    private ArrayList<DownloadLink> links = new ArrayList<DownloadLink>();

    public BMWLinkFetcher(String bmwURL) throws IOException, ParserException {

        NodeVisitor visitor = new NodeVisitor(true) {

            private Boolean dataField = false;
            private String currentCat = "";

            @Override
            public void visitTag(Tag tag) {

                String tagName = tag.getTagName();

                if(dataField){
                    if (tagName.equalsIgnoreCase("li")){
                        String linkName = tag.getChildren().elementAt(1).getChildren().elementAt(0).
                                getText().replaceAll("[\\t\\r\\n]", "").trim();// get text from a
                        String yearInfo = tag.getChildren().elementAt(4).getText().
                                replaceAll("[\\t\\r\\n]", "").replaceAll("since", "seit").trim();
                        
                        String link = ((Tag) tag.getChildren().elementAt(1)).getAttribute("href");
                        if (link != null && !link.startsWith("http://"))
                        {
                            if (!link.startsWith("/")) link = "/" + link.trim();
                            link = "https://oss.bmw.de" + link;
                        }
                        System.out.println("linkName: " + linkName + " year: " +  yearInfo + "\nLink: " + link);
                        links.add(new DownloadLink(link, "BMW" + File.separator + currentCat.trim().replaceAll("/", "_") + File.separator
                                + linkName.replaceAll("/", "_") + " " +
                                yearInfo.replaceAll("/", "_") +".pdf"));

                    }
                    else if (tagName.equalsIgnoreCase("h2")){
                        currentCat = tag.getChildren().elementAt(0).getText();
                        System.out.println("Categorie: " + currentCat);
                    }
                }
                else{
                    if(tagName.equalsIgnoreCase("table")) {
                        String classAt = tag.getAttribute("class");
                        if (classAt != null && classAt.equals("data")){
                            System.out.println("REACHED data field...");
                            dataField = true;
                        }
                    }
                }

            }
        };

        visitor.beginParsing();

        URL url = new URL(bmwURL);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; de-DE; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");

        Parser parser = new Parser(urlConnection);
        parser.visitAllNodesWith(visitor);


        /*NodeFilter filter = new HasAttributeFilter("class", "data");
        Node dataNode = parser.extractAllNodesThatMatch(filter).elementAt(0);
        System.out.println(dataNode.getText());

        NodeList h3s = dataNode.getChildren();

        for(NodeIterator iterator = h3s.elements(); iterator.hasMoreNodes();){
            Node trNode = iterator.nextNode();
            NodeList childs = trNode.getChildren();

            if (childs != null){

                for (NodeIterator it2 = childs.elements(); it2.hasMoreNodes(); ){
                    Node node = it2.nextNode();
                    Node firstChild = node.getFirstChild();
                    if (firstChild != null)
                        System.out.println(firstChild.getText());
                }
            }
        }*/
            
    }

    public ArrayList<DownloadLink> getLinks() {
        return links;
    }
}
