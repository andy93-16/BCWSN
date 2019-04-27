/**
 * This class is automatically generated by mig. DO NOT EDIT THIS FILE.
 * This class implements a Java interface to the 'SendTipMsg'
 * message type.
 */

public class SendTipMsg extends net.tinyos.message.Message {

    /** The default size of this message type in bytes. */
    public static final int DEFAULT_MESSAGE_SIZE = 2;

    /** The Active Message type associated with this message. */
    public static final int AM_TYPE = 12;

    /** Create a new SendTipMsg of size 2. */
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
        s += "  [tipHash=0x"+Long.toHexString(get_tipHash())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      return s;
    }

    // Message-type-specific access methods appear below.

    /////////////////////////////////////////////////////////
    // Accessor methods for field: tipHash
    //   Field type: int, unsigned
    //   Offset (bits): 0
    //   Size (bits): 16
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
        return (0 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'tipHash'
     */
    public static int offsetBits_tipHash() {
        return 0;
    }

    /**
     * Return the value (as a int) of the field 'tipHash'
     */
    public int get_tipHash() {
        return (int)getUIntBEElement(offsetBits_tipHash(), 16);
    }

    /**
     * Set the value of the field 'tipHash'
     */
    public void set_tipHash(int value) {
        setUIntBEElement(offsetBits_tipHash(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'tipHash'
     */
    public static int size_tipHash() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'tipHash'
     */
    public static int sizeBits_tipHash() {
        return 16;
    }

}