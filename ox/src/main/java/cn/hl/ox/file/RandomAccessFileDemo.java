package cn.hl.ox.file;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * RandomAccessFile是用来访问那些保存数据记录的文件的，你就可以用seek()方法来访问记录，并进行读写了。
 * 这些记录的大小不必相同；但是其大小和位置必须是可知的。但是该类仅限于操作文件。
 *
 * RandomAccessFile不属于InputStream和OutputStream类系的。实际上，
 * 除了实现DataInput和DataOutput接口之外
 * (DataInputStream和DataOutputStream也实现了这两个接口)，它和这两个类系毫不相干，
 * 甚至不使用InputStream和OutputStream类中已经存在的任何功能；
 * 它是一个完全独立的类，所有方法(绝大多数都只属于它自己)都是从零开始写的。
 * 这可能是因为RandomAccessFile能在文件里面前后移动，所以它的行为与其它的I/O类有些根本性的不同。
 * 总而言之，它是一个直接继承Object的，独立的类。
 * <br><br>
 * 基本上，RandomAccessFile的工作方式是，把DataInputStream和DataOutputStream结合起来，再加上它自己的一些方法，
 * 比如定位用的getFilePointer( )，在文件里移动用的seek( )，以及判断文件大小的length()、skipBytes()跳过多少字节数。
 * 此外，它的构造函数还要一个表示以只读方式("r")，还是以读写方式("rw")打开文件的参数(和C的fopen( )一模一样)。它不支持只写文件。
 * <br><br>
 * 只有RandomAccessFile才有seek搜寻方法，而这个方法也只适用于文件。BufferedInputStream有一个mark()方法，
 * 你可以用它来设定标记(把结果保存在一个内部变量里)，然后再调用reset( )返回这个位置，但是它的功能太弱了，而且也不怎么实用。
 * <br><br>
 * RandomAccessFile的绝大多数功能，但不是全部，已经被JDK1.4的nio的"内存映射文件(memory-mapped files)"给取代了，
 * 你该考虑一下是不是用"内存映射文件"来代替RandomAccessFile了。
 *
 * <br><hr><br>
 *
 * 其中mode 参数指定用以打开文件的访问模式。允许的值及其含意为：
 * <li>"r" 以只读方式打开。调用结果对象的任何 write 方法都将导致抛出 IOException。
 * <li>"rw" 打开以便读取和写入。如果该文件尚不存在，则尝试创建该文件。
 * <li>"rws" 打开以便读取和写入，对于 "rw"，还要求对文件的内容或元数据的每个更新都同步写入到底层存储设备。
 * <li>"rwd"  打开以便读取和写入，对于 "rw"，还要求对文件内容的每个更新都同步写入到底层存储设备。
 * <li>"rws" 和 "rwd" 模式的工作方式极其类似 FileChannel 类的 force(boolean) 方法，分别传递 true 和 false 参数，
 * 除非它们始终应用于每个 I/O 操作，并因此通常更为高效。如果该文件位于本地存储设备上，那么当返回此类的一个方法的调用时，
 * 可以保证由该调用对此文件所做的所有更改均被写入该设备。这对确保在系统崩溃时不会丢失重要信息特别有用。如果该文件不在本地设备上，则无法提供这样的保证。
 * <li>"rwd" 模式可用于减少执行的 I/O 操作数量。使用 "rwd" 仅要求更新要写入存储的文件的内容；
 * 使用 "rws" 要求更新要写入的文件内容及其元数据，这通常要求至少一个以上的低级别 I/O 操作。</li>
 *
 * <hr>如果存在安全管理器，则使用 file 参数的路径名作为其参数调用它的 checkRead 方法，以查看是否允许对该文件进行读取访问。
 * <br>如果该模式允许写入，那么还使用该路径参数调用该安全管理器的 checkWrite 方法，以查看是否允许对该文件进行写入访问。
 *
 */
public class RandomAccessFileDemo {
    public static void main(String[] args) throws IOException {
        RandomAccessFile file = new RandomAccessFile("_RandomAccessFileDemo.dat", "rw");
        file.setLength(0x100000);//1Mb
        // 以下向file文件中写数据
        file.writeInt(20);// 占4个字节
        file.writeDouble(8.236598);// 占8个字节
        file.writeUTF("这是一个UTF字符串");// 这个长度写在当前文件指针的前两个字节处，可用readShort()读取
        file.writeBoolean(true);// 占1个字节
        file.writeShort(395);// 占2个字节
        file.writeLong(2325451L);// 占8个字节
        file.writeUTF("又是一个UTF字符串");
        file.writeFloat(35.5f);// 占4个字节
        file.writeChar('a');// 占2个字节
        file.writeUTF(System.getProperty("line.separator"));

        // 把文件指针位置设置到文件起始处
        file.seek(0);
        file.writeInt(99);

        file.seek(0);
        // 以下从file文件中读数据，要注意文件指针的位置
        System.out.println("——————从file文件指定位置读数据——————");
        System.out.println(file.readInt());
        System.out.println(file.readDouble());
        System.out.println(file.readUTF());

        // 将文件指针跳过3个字节，本例中即跳过了一个boolean值和short值。
        file.skipBytes(3);
        System.out.println(file.readLong());

        // 跳过文件中“又是一个UTF字符串”所占字节，注意readShort()方法会移动文件指针，所以不用加2。
        file.skipBytes(file.readShort());
        System.out.println(file.readFloat());

        // 以下演示文件复制操作
        System.out.println("——————文件复制(从_RandomAccessFileDemo.dat到_RandomAccessFileCopy.dat)——————");
        file.seek(0);
        RandomAccessFile fileCopy = new RandomAccessFile("_RandomAccessFileCopy.dat", "rw");
        // 取得文件长度（字节数）
        int len = (int) file.length();
        byte[] b = new byte[len];
        file.readFully(b);
        fileCopy.write(b);
        System.out.println("复制完成！");
    }

}
