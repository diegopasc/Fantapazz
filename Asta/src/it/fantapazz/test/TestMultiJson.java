package it.fantapazz.test;

import it.fantapazz.asta.core.protocol.MsgChoose;
import it.fantapazz.chat.Message;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

class A {
	private String pippo;
	public A() {
	}
	public A(String pippo) {
		this.pippo = pippo;
	}
	public String getPippo() {
		return pippo;
	}
	public void setPippo(String pippo) {
		this.pippo = pippo;
	}
	@Override
	public String toString() {
		return "A [pippo=" + pippo + "]";
	}
}

class B extends A {
	private String pluto;
	public B() {
	}
	public B(String pippo, String pluto) {
		super(pippo);
		this.pluto = pluto;
	}
	public String getPluto() {
		return pluto;
	}
	public void setPluto(String pluto) {
		this.pluto = pluto;
	}
	@Override
	public String toString() {
		return "B [pluto=" + pluto + ", getPippo()=" + getPippo() + "]";
	}
}

public class TestMultiJson {
	
	public static String writeMessage(Message message) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", message.getClass().getName());
		map.put("object", message);
		return mapper.writeValueAsString(map);
	}
	
	public static Message readMessage(String value) throws JsonProcessingException, IOException, ClassNotFoundException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(value);
		String type = node.get("type").getTextValue();
		JsonNode object = node.get("object");
		Class<?> clazz = Class.forName(type);
		Message message = (Message) mapper.readValue(object, clazz);
		return message;
	}
	
	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException, ClassNotFoundException {
		
//		ZipOutputStream s2 = new ZipOutputStream(System.out);
//		ZipInputStream s1 = new ZipInputStream(null); 
		
		MsgChoose choose = new MsgChoose("Ciao");
		choose.setSource("Pippo");
		String value = writeMessage(choose);
		System.out.println(value);
		System.out.println(readMessage(value));
		
	}

}
