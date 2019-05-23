/**
 * This class is automatically generated by mig. DO NOT EDIT THIS FILE.
 * This class implements a Java interface to the 'SendTipMsg'
 * message type.
 */

package MsgClass;

public class SendTipMsg extends net.tinyos.message.Message {

    /** The default size of this message type in bytes. */
    public static final int DEFAULT_MESSAGE_SIZE = 20;

    /** The Active Message type associated with this message. */
    public static final int AM_TYPE = 12;

    /** Create a new SendTipMsg of size 20. */
    public SendTipMsg() {
        super(DEFAULT_MESSAGE_SIZE);
        amTypeSet(AM_TYPE);
    }

    /** Create a new SendTipMsg of the given data_length. */
    public SendTipMsg(int data_length) {
        super(data_length);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new SendTipMsg with the given data_length
     * and base offset.
     */
    public SendTipMsg(int data_length, int base_offset) {
        super(data_length, base_offset);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new SendTipMsg using the given byte array
     * as backing store.
     */
    public SendTipMsg(byte[] data) {
        super(data);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new SendTipMsg using the given byte array
     * as backing store, with the given base offset.
     */
    public SendTipMsg(byte[] data, int base_offset) {
        super(data, base_offset);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new SendTipMsg using the given byte array
     * as backing store, with the given base offset and data length.
     */
    public SendTipMsg(byte[] data, int base_offset, int data_length) {
        super(data, base_offset, data_length);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new SendTipMsg embedded in the given message
     * at the given base offset.
     */
    public SendTipMsg(net.tinyos.message.Message msg, int base_offset) {
        super(msg, base_offset, DEFAULT_MESSAGE_SIZE);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new SendTipMsg embedded in the given message
     * at the given base offset and length.
     */
    public SendTipMsg(net.tinyos.message.Message msg, int base_offset, int data_length) {
        super(msg, base_offset, data_length);
        amTypeSet(AM_TYPE);
    }

    /**
    /* Return a String representation of this message. Includes the
     * message type name and the non-indexed field values.
     */
    public String toString() {
      String s = "Message <SendTipMsg> \n";
      try {
        s += "  [nonce=0x"+Long.toHexString(get_nonce())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [tipHash=0x"+Long.toHexString(get_tipHash())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [temp=";
        for (int i = 0; i < 5; i++) {
          s += "0x"+Long.toHexString(getElement_temp(i) & 0xffff)+" ";
        }
        s += "]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      return s;
    }

    // Message-type-specific access methods appear below.

    /////////////////////////////////////////////////////////
    // Accessor methods for field: nonce
    //   Field type: int, unsigned
    //   Offset (bits): 0
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'nonce' is signed (false).
     */
    public static boolean isSigned_nonce() {
        return false;
    }

    /**
     * Return whether the field 'nonce' is an array (false).
     */
    public static boolean isArray_nonce() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'nonce'
     */
    public static int offset_nonce() {
        return (0 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'nonce'
     */
    public static int offsetBits_nonce() {
        return 0;
    }

    /**
     * Return the value (as a int) of the field 'nonce'
     */
    public int get_nonce() {
        return (int)getUIntBEElement(offsetBits_nonce(), 16);
    }

    /**
     * Set the value of the field 'nonce'
     */
    public void set_nonce(int value) {
        setUIntBEElement(offsetBits_nonce(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'nonce'
     */
    public static int size_nonce() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'nonce'
     */
    public static int sizeBits_nonce() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: tipHash
    //   Field type: long, unsigned
    //   Offset (bits): 16
    //   Size (bits): 64
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'tipHash' is signed (false).
     */
    public static boolean isSigned_tipHash() {
        return false;
    }

    /**
     * Return whether the field 'tipHash' is an array (false).
     */
    public static boolean isArray_tipHash() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'tipHash'
     */
    public static int offset_tipHash() {
        return (16 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'tipHash'
     */
    public static int offsetBits_tipHash() {
        return 16;
    }

    /**
     * Return the value (as a long) of the field 'tipHash'
     */
    public long get_tipHash() {
        return (long)getUIntBEElement(offsetBits_tipHash(), 64);
    }

    /**
     * Set the value of the field 'tipHash'
     */
    public void set_tipHash(long value) {
        setUIntBEElement(offsetBits_tipHash(), 64, value);
    }

    /**
     * Return the size, in bytes, of the field 'tipHash'
     */
    public static int size_tipHash() {
        return (64 / 8);
    }

    /**
     * Return the size, in bits, of the field 'tipHash'
     */
    public static int sizeBits_tipHash() {
        return 64;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: temp
    //   Field type: int[], unsigned
    //   Offset (bits): 80
    //   Size of each element (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'temp' is signed (false).
     */
    public static boolean isSigned_temp() {
        return false;
    }

    /**
     * Return whether the field 'temp' is an array (true).
     */
    public static boolean isArray_temp() {
        return true;
    }

    /**
     * Return the offset (in bytes) of the field 'temp'
     */
    public static int offset_temp(int index1) {
        int offset = 80;
        if (index1 < 0 || index1 >= 5) throw new ArrayIndexOutOfBoundsException();
        offset += 0 + index1 * 16;
        return (offset / 8);
    }

    /**
     * Return the offset (in bits) of the field 'temp'
     */
    public static int offsetBits_temp(int index1) {
        int offset = 80;
        if (index1 < 0 || index1 >= 5) throw new ArrayIndexOutOfBoundsException();
        offset += 0 + index1 * 16;
        return offset;
    }

    /**
     * Return the entire array 'temp' as a int[]
     */
    public int[] get_temp() {
        int[] tmp = new int[5];
        for (int index0 = 0; index0 < numElements_temp(0); index0++) {
            tmp[index0] = getElement_temp(index0);
        }
        return tmp;
    }

    /**
     * Set the contents of the array 'temp' from the given int[]
     */
    public void set_temp(int[] value) {
        for (int index0 = 0; index0 < value.length; index0++) {
            setElement_temp(index0, value[index0]);
        }
    }

    /**
     * Return an element (as a int) of the array 'temp'
     */
    public int getElement_temp(int index1) {
        return (int)getUIntBEElement(offsetBits_temp(index1), 16);
    }

    /**
     * Set an element of the array 'temp'
     */
    public void setElement_temp(int index1, int value) {
        setUIntBEElement(offsetBits_temp(index1), 16, value);
    }

    /**
     * Return the total size, in bytes, of the array 'temp'
     */
    public static int totalSize_temp() {
        return (80 / 8);
    }

    /**
     * Return the total size, in bits, of the array 'temp'
     */
    public static int totalSizeBits_temp() {
        return 80;
    }

    /**
     * Return the size, in bytes, of each element of the array 'temp'
     */
    public static int elementSize_temp() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of each element of the array 'temp'
     */
    public static int elementSizeBits_temp() {
        return 16;
    }

    /**
     * Return the number of dimensions in the array 'temp'
     */
    public static int numDimensions_temp() {
        return 1;
    }

    /**
     * Return the number of elements in the array 'temp'
     */
    public static int numElements_temp() {
        return 5;
    }

    /**
     * Return the number of elements in the array 'temp'
     * for the given dimension.
     */
    public static int numElements_temp(int dimension) {
      int array_dims[] = { 5,  };
        if (dimension < 0 || dimension >= 1) throw new ArrayIndexOutOfBoundsException();
        if (array_dims[dimension] == 0) throw new IllegalArgumentException("Array dimension "+dimension+" has unknown size");
        return array_dims[dimension];
    }

}
