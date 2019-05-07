/**
 * This class is automatically generated by mig. DO NOT EDIT THIS FILE.
 * This class implements a Java interface to the 'TipsRequestMsg'
 * message type.
 */

package MsgClass;

public class TipsRequestMsg extends net.tinyos.message.Message {

    /** The default size of this message type in bytes. */
    public static final int DEFAULT_MESSAGE_SIZE = 22;

    /** The Active Message type associated with this message. */
    public static final int AM_TYPE = 10;

    /** Create a new TipsRequestMsg of size 22. */
    public TipsRequestMsg() {
        super(DEFAULT_MESSAGE_SIZE);
        amTypeSet(AM_TYPE);
    }

    /** Create a new TipsRequestMsg of the given data_length. */
    public TipsRequestMsg(int data_length) {
        super(data_length);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new TipsRequestMsg with the given data_length
     * and base offset.
     */
    public TipsRequestMsg(int data_length, int base_offset) {
        super(data_length, base_offset);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new TipsRequestMsg using the given byte array
     * as backing store.
     */
    public TipsRequestMsg(byte[] data) {
        super(data);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new TipsRequestMsg using the given byte array
     * as backing store, with the given base offset.
     */
    public TipsRequestMsg(byte[] data, int base_offset) {
        super(data, base_offset);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new TipsRequestMsg using the given byte array
     * as backing store, with the given base offset and data length.
     */
    public TipsRequestMsg(byte[] data, int base_offset, int data_length) {
        super(data, base_offset, data_length);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new TipsRequestMsg embedded in the given message
     * at the given base offset.
     */
    public TipsRequestMsg(net.tinyos.message.Message msg, int base_offset) {
        super(msg, base_offset, DEFAULT_MESSAGE_SIZE);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new TipsRequestMsg embedded in the given message
     * at the given base offset and length.
     */
    public TipsRequestMsg(net.tinyos.message.Message msg, int base_offset, int data_length) {
        super(msg, base_offset, data_length);
        amTypeSet(AM_TYPE);
    }

    /**
    /* Return a String representation of this message. Includes the
     * message type name and the non-indexed field values.
     */
    public String toString() {
      String s = "QUESTO MESSAGGIO E' DI TIPO: <TipsRequestMsg> \n";
      try {
        s += "  [nodeid=0x"+Long.toHexString(get_nodeid())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [temp=";
        for (int i = 0; i < 10; i++) {
          s += "0x"+Long.toHexString(getElement_temp(i) & 0xffff)+" ";
        }
        s += "]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      return s;
    }

    // Message-type-specific access methods appear below.

    /////////////////////////////////////////////////////////
    // Accessor methods for field: nodeid
    //   Field type: int, unsigned
    //   Offset (bits): 0
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'nodeid' is signed (false).
     */
    public static boolean isSigned_nodeid() {
        return false;
    }

    /**
     * Return whether the field 'nodeid' is an array (false).
     */
    public static boolean isArray_nodeid() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'nodeid'
     */
    public static int offset_nodeid() {
        return (0 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'nodeid'
     */
    public static int offsetBits_nodeid() {
        return 0;
    }

    /**
     * Return the value (as a int) of the field 'nodeid'
     */
    public int get_nodeid() {
        return (int)getUIntBEElement(offsetBits_nodeid(), 16);
    }

    /**
     * Set the value of the field 'nodeid'
     */
    public void set_nodeid(int value) {
        setUIntBEElement(offsetBits_nodeid(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'nodeid'
     */
    public static int size_nodeid() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'nodeid'
     */
    public static int sizeBits_nodeid() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: temp
    //   Field type: int[], unsigned
    //   Offset (bits): 16
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
        int offset = 16;
        if (index1 < 0 || index1 >= 10) throw new ArrayIndexOutOfBoundsException();
        offset += 0 + index1 * 16;
        return (offset / 8);
    }

    /**
     * Return the offset (in bits) of the field 'temp'
     */
    public static int offsetBits_temp(int index1) {
        int offset = 16;
        if (index1 < 0 || index1 >= 10) throw new ArrayIndexOutOfBoundsException();
        offset += 0 + index1 * 16;
        return offset;
    }

    /**
     * Return the entire array 'temp' as a int[]
     */
    public int[] get_temp() {
        int[] tmp = new int[10];
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
        return (160 / 8);
    }

    /**
     * Return the total size, in bits, of the array 'temp'
     */
    public static int totalSizeBits_temp() {
        return 160;
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
        return 10;
    }

    /**
     * Return the number of elements in the array 'temp'
     * for the given dimension.
     */
    public static int numElements_temp(int dimension) {
      int array_dims[] = { 10,  };
        if (dimension < 0 || dimension >= 1) throw new ArrayIndexOutOfBoundsException();
        if (array_dims[dimension] == 0) throw new IllegalArgumentException("Array dimension "+dimension+" has unknown size");
        return array_dims[dimension];
    }

}
