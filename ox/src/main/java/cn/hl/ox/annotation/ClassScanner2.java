package cn.hl.ox.annotation;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.FileSystem;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.spi.FileSystemProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author hyman
 * @date 2021-01-05 11:05:12
 */
public final class ClassScanner2 {
    private ClassScanner2() {
    }

    static public byte[] read(InputStream in) throws IOException {
        byte[] temp = new byte[4096];
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        BufferedInputStream bin = new BufferedInputStream(in);
        for (; ; ) {
            int len = bin.read(temp);
            if (len > -1) {
                buffer.write(temp, 0, len);
            } else {
                break;
            }
        }
        return buffer.toByteArray();
    }

    static public String[] scanPackage(String packageName) throws Exception {
        packageName = packageName.replaceAll("\\.", "/");
        URL url = Thread.currentThread().getContextClassLoader().getResource(packageName);
        url = new URL(URLDecoder.decode(url.toString(), "UTF-8"));
        FileSystemProvider provider = null;
        if (url.getProtocol().equals("jar")) {
            provider = getZipFSProvider();
            if (provider != null) {
                try (FileSystem fs = provider.newFileSystem(Paths.get(url.getPath().replaceFirst("file:/", "").replaceFirst("!.*", "")),
                        new HashMap<>())) {
                    return walkFileTree(fs.getPath(packageName), null).toArray(new String[0]);
                } catch (Exception e) {
                    throw e;
                }
            }
        } else if (url.getProtocol().equals("file")) {
            int end = url.getPath().lastIndexOf(packageName);
            String basePath = url.getPath().substring(1, end);
            return walkFileTree(Paths.get(url.getPath().replaceFirst("/", "")), Paths.get(basePath)).toArray(new String[0]);
        }
        return null;
    }

    private static List<String> walkFileTree(Path path, Path basePath) throws IOException {
        final List<String> result = new ArrayList<>();
        java.nio.file.Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            private String packageName = Objects.isNull(basePath) ? "" : basePath.toString();

            /* (non-Javadoc)
             * @see java.nio.file.SimpleFileVisitor#visitFile(java.lang.Object, java.nio.file.attribute.BasicFileAttributes)
             */
            @Override
            public FileVisitResult visitFile(Path arg0, BasicFileAttributes arg1) throws IOException {
                if (arg0.toString().endsWith(".class")) {
                    result.add(arg0.toString().replace(packageName, "").substring(1).replace("\\", "/").replace(".class", "")
                            .replace("/", "."));
                }
                return FileVisitResult.CONTINUE;
            }

            /* (non-Javadoc)
             * @see java.nio.file.SimpleFileVisitor#preVisitDirectory(java.lang.Object, java.nio.file.attribute.BasicFileAttributes)
             */
            @Override
            public FileVisitResult preVisitDirectory(Path arg0, BasicFileAttributes arg1) throws IOException {
                //packageName=basePath==null?arg0.toString().substring(1, arg0.toString().length()-1).replace("/", "."):basePath
                // .relativize(arg0).toString().replace("\\", ".");
                return FileVisitResult.CONTINUE;
            }

        });
        return result;
    }

    static FileSystemProvider getZipFSProvider() {
        for (FileSystemProvider provider : FileSystemProvider.installedProviders()) {
            if ("jar".equals(provider.getScheme())) {
                return provider;
            }
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(Paths.get("/Users/hyman/ws_idea/Tofu/ox/target/classes/cn/hl/ox/annotation"));
        File dir = new File("/Users/hyman/ws_idea/Tofu/ox/target/classes/cn/hl/ox/annotation");
        System.out.println(dir.exists());
        // System.out.println(new URL("jar:file:/home/whf/foo.jar!/cn/fh").getPath());
        for (String c : scanPackage("cn.hl.ox.annotation")) {
            System.out.println(c);
        }
    }

}