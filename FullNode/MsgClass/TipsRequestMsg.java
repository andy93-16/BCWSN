/**
 * This class is automatically generated by mig. DO NOT EDIT THIS FILE.
 * This class implements a Java interface to the 'TipsRequestMsg'
 * message type.
 */

package MsgClass;

public class TipsRequestMsg extends net.tinyos.message.Message {

    /** The default size of this message type in bytes. */
    public static final int DEFAULT_MESSAGE_SIZE = 0;

    /** The Active Message type associated with this message. */
    public static final int AM_TYPE = 10;

    /** Create a new TipsRequestMsg of size 0. */
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
      String s = "Message <TipsRequestMsg> \n";
      return s;
    }

    // Message-type-specific access methods appear below.

}
