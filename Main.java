import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Main {
	
	private static final String writableFile = "src\\main\\java\\staff.json";
	private static final String fileToRead = "src\\main\\java\\data.xml";
	
	public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
		List<Employee> list = parseXML(fileToRead);
		String json = listToJson(list);
		
		createFile();
		writeString(json);
	}
	
	private static List<Employee> parseXML(String file) throws ParserConfigurationException, IOException, SAXException {
		List<Employee> emloyees = new ArrayList<>();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new File(file));
		
		Node root = doc.getDocumentElement();
		NodeList nodeList = root.getChildNodes();
		
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node nodes = nodeList.item(i);
			
			if (nodes.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) nodes;
				
				long id = Long.parseLong(element.getElementsByTagName("id").item(0).getTextContent());
				String firstName = element.getElementsByTagName("firstName").item(0).getTextContent();
				String lastName = element.getElementsByTagName("lastName").item(0).getTextContent();
				String country = element.getElementsByTagName("country").item(0).getTextContent();
				int age = Integer.parseInt(element.getElementsByTagName("age").item(0).getTextContent());
				
				emloyees.add(new Employee(id, firstName, lastName, country, age));
			}
		}
		return emloyees;
	}
	
	private static String listToJson(List<Employee> list) {
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		Type listType = new TypeToken<List<Type>>() {
		}.getType();
		return gson.toJson(list, listType);
	}
	
	private static void writeString(String json) {
		try (FileWriter writer = new FileWriter(writableFile)) {
			writer.write(json);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	private static void createFile() {
		File newDir = new File(writableFile);
		try {
			newDir.createNewFile();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}