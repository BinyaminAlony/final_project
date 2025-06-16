package project_biu.graph;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * The Message class represents a message, that an agent can send to a topic.
 * It supports multiple data types, including byte arrays, strings, and doubles.
 */
public class Message {
    public final byte[] data;
    public final String asText;
    public final Double asDouble;
    public final Date date;
    
    /**
     * Constructs a Message from a byte array.
     *
     * @param byteArr The byte array representing the message.
     */
    public Message(byte[] byteArr) {
		this.data = byteArr;
		this.asText = new String(byteArr, StandardCharsets.UTF_8);
		this.date = getCurrentDate();
		Double tempDouble;
		try {
			tempDouble = ByteBuffer.wrap(byteArr).getDouble();			
		} catch (Exception e) {
//			System.out.println("Couldnt cast to Double. asDouble will return null.");
			tempDouble = Double.NaN;
		}
		this.asDouble = tempDouble;
	}
    
    /**
     * Constructs a Message from a string.
     *
     * @param str The string representing the message.
     */
	public Message(String str) {
		this.data = str.getBytes();
		this.asText = str;
		this.date = getCurrentDate();
		Double tempDouble;
		try {
			tempDouble = Double.parseDouble(str);			
		} catch (Exception e) {
//			System.out.println("Couldnt cast to Double. asDouble will return null.");
			tempDouble = Double.NaN;
		}
		this.asDouble = tempDouble;
	}
    
    /**
     * Constructs a Message from a double.
     *
     * @param d The double representing the message.
     */
	public Message(Double d) {
		ByteBuffer buffer = ByteBuffer.allocate(Double.BYTES); // Double.BYTES = 8
        buffer.putDouble(d);
        this.data = buffer.array();
		this.asText = String.valueOf(d);
		this.date = getCurrentDate();
		this.asDouble = d;
	}
	
	//because apparently i need to support ints as well.
	public Message(int value) { 
	    this((double) value); // Call the Double constructor
	}
	
	private Date getCurrentDate() {
		return new Date(System.currentTimeMillis());
	}
	
//	public void printMsg() {
//		System.out.println("data:" + this.data);
//		System.out.println("as text:" + this.asText);
//		System.out.println("as Double:" + this.asDouble);
//		System.out.println("date:" + this.date);
//		System.out.println("");
//	}
}
