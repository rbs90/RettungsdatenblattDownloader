import org.htmlparser.util.ParserException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: rbs
 * Date: 16.03.12
 * Time: 09:50
 * To change this template use File | Settings | File Templates.
 */
public class MainFrame extends JFrame{

    public static final String bmwURL = "https://oss.bmw.de/changeLanguage.do?language=de&country=DE&languageChange=true&previousActionForward=/carEmergencyServiceCards.do?method=create";
    public static final String peugeotURL = "http://services.peugeot.de/rettungsblaetter/";
    public static ArrayList<DownloadLink> linkList = new ArrayList<DownloadLink>();
    private static JTextArea textArea;
    private static JProgressBar progressBar;
    private JScrollPane scrollPane;

    public MainFrame() {
        this.setLayout(new BorderLayout());
        textArea = new JTextArea();
        progressBar = new JProgressBar();
        JButton startButton = new JButton("Start");

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ActionStarter().start();
            }
        });

        scrollPane = new JScrollPane(textArea);
        
        add(scrollPane, BorderLayout.CENTER);
        add(progressBar, BorderLayout.NORTH);
        add(startButton, BorderLayout.SOUTH);
        setVisible(true);

        setPreferredSize(new Dimension(500, 500));
        setSize(new Dimension(500, 500));
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new MainFrame();
    }

    private void addLog(String logMes){
        textArea.append(logMes);
        textArea.setCaretPosition(textArea.getText().length());
    }
    
    private class ActionStarter extends Thread {

        @Override
        public void run() {
            progressBar.setIndeterminate(true);
            addLog("Hohle URLs von BMW...\n");
            try {
                ArrayList<DownloadLink> links = new BMWLinkFetcher(bmwURL).getLinks();
                addLog(links.size() + " Links von BMW geladen.\n");
                linkList.addAll(links);

                addLog("Hohle URLs von Peugeot...\n");
                links = new PeugeotLinkFetcher(peugeotURL).getLinks();
                addLog(links.size() + " Links von Peugeot geladen.\n");
                linkList.addAll(links);

            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (ParserException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            addLog("****************************************" +
                          "\nAlle Links geholt. Beginne mit Download:\n" +
                            "****************************************\n");

            progressBar.setMinimum(0);
            progressBar.setMaximum(linkList.size());
            progressBar.setIndeterminate(false);

            for (DownloadLink link: linkList){
                addLog("Lade " + link.getFileName() + "...");
                if (URLDownloader.loadToFile(link))
                    addLog("OK");
                else
                    addLog("Fehler");
                addLog("\n");
                progressBar.setValue(progressBar.getValue() + 1);
            }

            addLog("****************************************" +
                          "\n                FERTIG                  \n" +
                            "****************************************\n");

        }
    }
}
