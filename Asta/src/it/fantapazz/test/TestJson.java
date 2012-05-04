package it.fantapazz.test;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

class Test {
	
	private String pippo;

	public String getPippo() {
		return pippo;
	}

	public void setPippo(String pippo) {
		this.pippo = pippo;
	}

	@Override
	public String toString() {
		return "Test [pippo=" + pippo + "]";
	}
	
}

public class TestJson {
	
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		
		ObjectMapper mapper = new ObjectMapper();
		
		Test test = new Test();
		
		test.setPippo("Ciao mamma");
		
		String value = mapper.writeValueAsString(test);
		
		System.out.println(value);
		
		Test test1 = mapper.readValue(value, Test.class);
		
		System.out.println(test1);
		
//		InfoI connector = FantapazzConnector.getInfo();
//				
//		System.out.println(connector.getCalciatore("1"));
//
//		System.out.println(connector.getLega("5"));

	}

}
