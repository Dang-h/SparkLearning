package basic.interfacceTest;

public class Test3 {
	public static void main(String[] args) {

	}
}



class AsciiCharSequence implements java.lang.CharSequence{
	byte[] bytes;

	public AsciiCharSequence(byte[] bytes) {
		this.bytes = bytes;
	}

	@Override
	public int length() {
		return bytes.length;
	}

	@Override
	public char charAt(int index) {
		return (char)bytes[index];
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		String string = new String(bytes);
		String substring = string.substring(start, end);
		byte[] bytes = substring.getBytes();

//		return new AsciiCharSequence(java.util.Arrays.copyOfRange(byteSequence, start, end));
		return new AsciiCharSequence(bytes);
	}

	@Override
	public String toString() {
		return new String(bytes);
	}
}
