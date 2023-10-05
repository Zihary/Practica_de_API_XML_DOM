import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import java.io.File;
import java.util.Scanner;
import java.text.DecimalFormat;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class API {
    public static void main(String[] args) {
        try {
            File inputFile = new File("sales.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            Scanner scanner = new Scanner(System.in);
            System.out.print("Ingrese el porcentaje de incremento (entre 5% y 15%): ");
            double porcentaje = scanner.nextDouble();

            if (porcentaje < 5 || porcentaje > 15) {
                System.out.println("El porcentaje debe estar entre 5% y 15%.");
                return;
            }

            scanner.nextLine();

            System.out.print("Ingrese el departamento a modificar: ");
            String departamento = scanner.nextLine();

            NodeList saleRecords = doc.getElementsByTagName("sale_record");

            for (int i = 0; i < saleRecords.getLength(); i++) {
                Node saleRecord = saleRecords.item(i);

                if (saleRecord.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) saleRecord;

                    String currentDepartment = element.getElementsByTagName("department").item(0).getTextContent();

                    if (currentDepartment.equals(departamento)) {
                        double currentSales = Double.parseDouble(element.getElementsByTagName("sales").item(0).getTextContent());
                        double newSales = currentSales * (1 + porcentaje / 100);

                        DecimalFormat df = new DecimalFormat("#.##");
                        element.getElementsByTagName("sales").item(0).setTextContent(df.format(newSales));
                    }
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("new_sales.xml"));

            transformer.transform(source, result);

            System.out.println("El archivo new_sales.xml se ha generado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
