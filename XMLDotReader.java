// XMLDotReader.java
// Uses the SAX interface
import java.io.*;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;


public class XMLDotReader extends DefaultHandler {

    public static void main(String argv[]) {
        try {
            DefaultHandler dotsHandler = new XMLDotReader();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            //use default non-validating parser
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(new File("/Users/allenpianoman/Desktop/PASSC_tema4/src/main/java/dots.xsd"), dotsHandler);
        } catch (Throwable t) { // much too general exception handling !!!
            t.printStackTrace();
        }
    }
    public void startDocument() throws SAXException {}

    public void endDocument() throws SAXException {}

    private boolean baseElem = false;
    private boolean complex = false;
    private boolean sequence = false;
    private boolean attributes = false;
    private String sName;
    private String auxName;
    private String baseName;
    private String childName;
    private String sType;
    private String maxOccurs;
    private String fields="";
    private String getters="";
    private String setters="";
    private String factory="";
    private String code="";
    private String imports="";
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        //System.out.println("start element:" + qName);
        if (qName.equals("xs:schema")) {
            baseElem = true;
            complex = false;
            //System.out.println("We have a schema");
        }
        if (qName.equals("xs:element") ) {
            sName = atts.getValue("name");
            if(baseElem) {
                auxName = sName.substring(0, 1).toUpperCase() + sName.substring(1);
                code+="\n\nclass "+auxName+" {";
                //System.out.println("baseElement: " + sName);
                baseName = auxName;
                baseElem=false;
            }
            if(sequence){
                //System.out.println("sequence: "+sName);
                auxName = sName.substring(0, 1).toUpperCase() + sName.substring(1);
                childName = auxName;
                maxOccurs = atts.getValue("maxOccurs");
                if(maxOccurs.equals("unbounded")) {
                    //System.out.println("List needed");
                    code+="\n\tprotected List<"+childName+"> "+sName+";";
                    code+="\n\tpublic List<"+baseName+"."+childName+"> get"+childName+"() {\n" +
                            "\t\t\tif ("+sName+" == null) {\n" +
                            "\t\t\t"+sName+" = new ArrayList<"+childName+">();\n" +
                            "\t\t}\n" +
                            "\t\treturn this."+sName+";\n" +
                            "\t}\n\t"+
                            "public static class "+childName+" {";
                    imports = "import java.util.ArrayList;\nimport java.util.List;";

                    //System.out.println(imports);
                }
            }
        } else if(qName.equals("xs:complexType")) {
            complex = true;
        } else if(qName.equals("xs:sequence")){
            sequence=true;
        } else if (qName.equals("xs:attribute")) {
            sName = atts.getValue("name");
            sType = atts.getValue("type");
            auxName = sName.substring(0, 1).toUpperCase() + sName.substring(1);
            if(sequence) {
                attributes=true;
                if (sType.equals("xs:integer")) {
                    //System.out.println("attribute: " + sName + " type: " + int.class);
                    fields+="\n\t\tprotected Integer "+sName+";";
                    getters+="\n\t\tpublic Integer get"+auxName+"() {return "+sName+";}\n";
                    setters+="\n\t\tpublic void set"+auxName+"(Integer value) {this."+sName+" = value;}\n";
                } else if (sType.equals("xs:string")) {
                    //System.out.println("attribute: " + sName + " type: " + String.class.getTypeName());
                    fields+="\n\t\tprotected String "+sName+";";
                    getters+="\n\t\tpublic String get"+auxName+"() {return "+sName+";}\n";
                    setters+="\n\t\tpublic void set"+auxName+"(String value) {this."+sName+" = value;}\n";
                }
            }
        }

    }


    // Called at the end of each element
    public void endElement(java.lang.String uri, java.lang.String localName, java.lang.String qName) throws SAXException {
        //System.out.println("end element:" + qName);
        if(qName.equals("xs:complexType")) {
            if(attributes){
                code+="\n"+fields + getters + setters+"\n\t}";
                fields="";
                getters="";
                setters="";
                attributes=false;
                factory+="\n\tpublic "+baseName+"."+childName+" create"+baseName+childName+"() {\n" +
                        "\t\treturn new "+baseName+"."+childName+"();\n\t}\n\n";
            }
        }
        if(qName.equals("xs:schema")){
            code+="\n}\n\nclass ObjectFactory {\n\tpublic ObjectFactory() {}\n" +
                    factory +
                    "\tpublic "+baseName+" create"+baseName+"() {\n" +
                    "\t\treturn new "+baseName+"();\n\t}\n}";
            code = imports.concat(code);
            System.out.println(code);
            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter("/Users/allenpianoman/Desktop/PASSC_tema4/src/main/java/Dots.java");
            } catch (IOException e) {
                e.printStackTrace();
            }
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(code);
            printWriter.close();
        }
    }

    // Called for characters between nodes.
    public void characters(char buf[], int offset, int len) throws SAXException {
        String s = new String(buf, offset, len);
        s = s.trim();
        if (!s.equals("")) {
           // System.out.println("characters:" + s);
        }
    }
}
