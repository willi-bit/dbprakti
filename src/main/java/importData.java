import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class importData {
    public static void importDataFromFile(String filePath) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        FileInputStream fileIS = new FileInputStream(filePath);
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document xmlDocument = builder.parse(fileIS);
        xmlDocument.normalizeDocument();
        NodeList nodeList = (NodeList) xmlDocument.getElementsByTagName("category");
        for (int i = 0; i < 2; i++) {
            Node n = nodeList.item(i);
            NodeList nList = (NodeList) n.getChildNodes();
            for(int j = 0; j < nList.getLength(); j++) {
                Node kek = nList.item(j);
            }
        }
        /**
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        factory.setIgnoringElementContentWhitespace(true);
        try {
            XMLErrorHandler xml = new XMLErrorHandler();
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(xml);
            File file = new File(filePath);
            System.out.println("Parsing " + file.getAbsolutePath());
            Document doc = builder.parse(file);
        } catch (ParserConfigurationException | IOException e) {
            System.out.println("ERROROROROROR");
            throw new RuntimeException(e);
        } catch (SAXException e) {
            System.out.println(e);
         }**/
    }
}
