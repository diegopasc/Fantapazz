package it.fantapazz.utility;

import it.fantapazz.ConfigSettings;
import it.fantapazz.chat.Message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

public class MessageSerializer {
	
	public static byte ZIPPED = 100;
	public static byte NON_ZIPPED = 50;
	
	public static byte[] zip(byte[] value) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ZipOutputStream zout = new ZipOutputStream(out);
		zout.putNextEntry(new ZipEntry("object.zip"));
		zout.write(value);
		zout.closeEntry();
		zout.flush();
		zout.close();
		return out.toByteArray();
	}
	
	public static byte[] unzip(byte[] value) throws IOException {
		ZipInputStream zin = new ZipInputStream(new ByteArrayInputStream(value));
		ZipEntry entry = zin.getNextEntry();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		FileUtility.joinStreams(zin, out);
		zin.closeEntry();
		out.close();
		zin.close();
		return out.toByteArray();
	}
	
	public static byte[] tryZip(byte[] value) throws IOException {
		byte[] buffer = zip(value);
		byte[] ret;
		System.out.println("Value: " + value.length + ", Zipped: " + buffer.length);
		if ( value.length > buffer.length ) {
			if ( ConfigSettings.instance().isActivateStats() ) {
				StatsCommunication.instance().addRatio( (double) buffer.length / (double) value.length * 100.0 );
			}
			ret = new byte[buffer.length + 1];
			ret[0] = ZIPPED;
			System.arraycopy(buffer, 0, ret, 1, buffer.length);
		}
		else {
			ret = new byte[value.length + 1];
			ret[0] = NON_ZIPPED;
			System.arraycopy(value, 0, ret, 1, value.length);
		}
		return ret;
	}
	
	public static byte[] tryUnzip(byte[] value) throws IOException {
		byte[] buffer = new byte[value.length - 1];
		System.arraycopy(value, 1, buffer, 0, value.length - 1);
		if ( value[0] == NON_ZIPPED )
			return buffer;
		return unzip(buffer);
	}

	public static String writeMessage(Message message) throws JsonGenerationException, JsonMappingException, IOException {
		long now = System.currentTimeMillis();
		int bytes = 0;
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", message.getClass().getName());
		if ( ConfigSettings.instance().isEnableZip() ) {
			String value = mapper.writeValueAsString(message);
			bytes = value.getBytes().length;
			map.put("object", MessageSerializer.tryZip(value.getBytes()));
		}
		else {
			map.put("object", message);
		}
		String ret = mapper.writeValueAsString(map);
		long end = System.currentTimeMillis();
		if (bytes > 0) {
			StatsCommunication.instance().addTimeMarshal(bytes, (end - now));
		}
		return ret;
	}
	
	public static Message readMessage(String value) throws JsonProcessingException, IOException, ClassNotFoundException {
		long now = System.currentTimeMillis();
		int bytes = 0;
		ObjectMapper mapper = new ObjectMapper();
		JsonNode map = mapper.readTree(value);
		String type = map.get("type").getTextValue();
		Class<?> clazz = Class.forName(type);
		Message message;
		if ( ConfigSettings.instance().isEnableZip() ) {
			byte[] msg = map.get("object").getBinaryValue();
			bytes = msg.length;
			String object = new String(MessageSerializer.tryUnzip(msg));
			message = (Message) mapper.readValue(object, clazz);
		}
		else {
			message = (Message) mapper.readValue(map.get("object"), clazz);
		}
		long end = System.currentTimeMillis();
		if (bytes > 0) {
			StatsCommunication.instance().addTimeUnmarshal(bytes, (end - now));
		}
		return message;
	}

}
