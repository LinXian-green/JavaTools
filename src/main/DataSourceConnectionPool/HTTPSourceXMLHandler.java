package DataSourceConnectionPool;
import com.google.common.base.Preconditions;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.source.http.HTTPSourceHandler;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sun.plugin.dom.core.Document;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HTTPSourceXMLHandler implements HTTPSourceHandler {
    private final String ROOT = "events";
    private final String EVENT_TAG = "event";
    private final String HEADERS_TAG = "headers";
    private final String BODY_TAG = "body";

    private final String CONF_INSERT_TIMESTAMP = "insertTimestamp";
    private final String TIMESTAMP_HEADER = "timestamp";

    private final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

    private final ThreadLocal<DocumentBuilder> doBuilder = new ThreadLocal<DocumentBuilder>();

    private boolean insertTimestamp;

    @Override
    public List<Event> getEvents(HttpServletRequest httpServletRequest) throws ParserConfigurationException {
        if(doBuilder.get() == null) {
            doBuilder.set(documentBuilderFactory.newDocumentBuilder());
        }

        Document doc;
        final List<Event> events;
        doc = doBuilder.get().parse(httpServletRequest.getInputStream());
        Element root = doc.getDocumentElement();
        root.normalize();

        Preconditions.checkState(ROOT.equalsIgnoreCase((root.getTagName())));
        NodeList nodes = root.getElementsByTagName(EVENT_TAG);
        int eventCount = nodes.getLength();
        events = new ArrayList<Event>(eventCount);
        for(int i = 0; i < eventCount; i++){
            Element event = nodes.item(i);
            NodeList headerNodes = event.getElementsByTagName(HEADERS_TAG);
            Map<String,String> eventHeaders = new HashMap<String,String>();
            for(int j = 0; j < headerNodes.getLength(); j++){
                Node headerNode = headerNodes.item(j);
                NodeList headers = headerNode.getChildNodes();
                for(int k = 0; k < headers.getLength(); k++){
                    Node header = headers.item(k);

                    if(headerNode.getNodeType() != Node.ELEMENT_NODE){
                        continue;
                    }
                    Preconditions.checkState(!eventHeaders.containsKey(header.getNodeName()),"Header expevted only once" + header.getNodeName());
                    eventHeaders.put(header.getNodeName(),header.getTextContent());
                }
            }
        }
        Node body = event

    }


    @Override
    public void configure(Context context) {

    }
}
