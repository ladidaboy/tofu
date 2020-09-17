package cn.hl.ax.snmp.mib;

import cn.hl.ax.CommonConst;
import org.snmp4j.smi.OID;

/**
 * @author hyman
 * @date 2019-08-05 16:18:52
 * @version $ Id: Mib.java, v 0.1  hyman Exp $
 */
public class Mib {
    private int[]  oid;
    private String name;
    private String dataType;
    private int    accessMode;
    private String description;
    private String oidText;

    public Mib(String name, int[] oid, String dataType, int accessMode, String description) {
        this.name = name;
        this.oid = oid;
        this.dataType = dataType;
        this.accessMode = accessMode;
        this.description = description;
        this.oidText = formatOid();
    }

    public int[] child(int i) {
        if (i == 0) {
            return oid.clone();
        }
        int len = oid.length;
        int[] childOid = new int[len + 1];
        System.arraycopy(oid, 0, childOid, 0, len);
        childOid[len] = i;
        return childOid;
    }

    public boolean canRead() {
        return AccessMode.Read.value == (accessMode & AccessMode.Read.value);
    }

    public boolean canWrite() {
        return AccessMode.Write.value == (accessMode & AccessMode.Write.value);
    }

    public OID getOID() {
        return new OID(oid);
    }

    public int[] getOid() {
        return oid;
    }

    public String getOidText() {
        return oidText;
    }

    public String getName() {
        return name;
    }

    public String getDataType() {
        return dataType;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOid(int[] oid) {
        this.oid = oid;
        this.oidText = formatOid();
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public void setAccessMode(int accessMode) {
        this.accessMode = accessMode;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Mib{" + "name='" + name + "', oid='" + getOidText() + "', dataType='" + dataType + "', accessMode=" + accessMode
                + ", description='" + description + "'}";
    }

    private String formatOid() {
        if (oid == null) {
            return CommonConst.S_NIL;
        }
        int iMax = oid.length - 1;
        if (iMax == -1) {
            return ".";
        }

        StringBuilder b = new StringBuilder();
        for (int i = 0; ; i++) {
            b.append(oid[i]);
            if (i == iMax) {
                return b.toString();
            }
            b.append('.');
        }
    }
    // ------------------------------------------------------------------------------------------------------------------------

    /*
     常用对象类型 
     ----v1----
     (1)简单类型：Integer、Octet String、Object Identifier、Null
     (2)应用类型：IpAddress、Counter、Gauge、TimeTicks、Opaque
     ----v2----
     (1)简单类型：Integer32、Octet String、Object Identifier、Null
     (2)应用类型：IpAddress、Counter32、Counter64、Gauge32、Unsigned32、TimeTicks、Opaque、BIT STRING
     */
    private enum DataType {
        /**整型 Signed 32bit Integer (values between -2147483648 and 2147483647). 有符号32位整数（值范围: -2147483648 - +2147483648）*/
        Integer,
        /**Same as Integer. 与Integer相同。*/
        Integer32,
        /**Unsigned 32bit Integer (values between 0 and 4294967295). 无符号32位整数（值范围：0－4294967295）*/
        UInteger32,
        /**Arbitrary binary or textual data, typically limited to 255 characters in length. 任意二进制或文本数据，通常长度限制在255个字符内。(DisplayString[255])*/
        OctetString,
        /**An OID. 一个OID。*/
        ObjectIdentifier,
        /**Represents an enumeration of named bits. This is an unsigned datatype. 表示取名的位的枚举。这是一个无符号的数据类型。*/
        BitString,
        /**An IP address. 一个IP地址。*/
        IpAddress,
        /**Represents a non-negative integer which monotonically increases until it reaches a maximum value of 32bits-1 (4294967295 dec),
         * when it wraps around and starts increasing again from zero. 表示一个非负的整数（可递增到32位最大值－1），然后恢复并从0开始递增。*/
        Counter32,
        /**Same as Counter32 but has a maximum value of 64bits-1. 与Counter32相同，最大值为64位的最大值－1。*/
        Counter64,
        /**Represents an unsigned integer, which may increase or decrease, but shall never exceed a maximum value.
         * 表示无符号整数，可增加或减少，但是不超过最大值。*/
        Gauge32,
        /**Represents an unsigned integer which represents the time, modulo 2ˆ32 (4294967296 dec),
         * in hundredths of a second between two epochs.
         * 表示代表数据的一个无符号整数，2^32取模（4294967296），两个值之间为百分之一秒。*/
        TimeTicks,
        /**Provided solely for backward-compatibility, its no longer used. 提供向下兼容，不再使用的数据类型*/
        Opaque,
        /**Represents an OSI address as a variable-length OCTET STRING. 表示一个用变长八进制字符窗表示的OSI地址。*/
        NsapAddress;
    }

    public enum AccessMode {
        /**不可访问*/
        None(0),
        /**可读*/
        Read(1),
        /**可写*/
        Write(2),
        /**读写*/
        All(3);

        private int value;

        AccessMode(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }
}
