import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class UniqueWordCheckerLoader extends ClassLoader {
    private static final String LIB_PATH = "word10/IPJ.jar";

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            JarFile jarLib = new JarFile(LIB_PATH);
            JarEntry jarEntry = jarLib.getJarEntry(
                    name.replace(".", "/") + ".class");
            InputStream libInputStream = jarLib.getInputStream(jarEntry);

            byte[] classBytes = new byte[(int) jarEntry.getSize()];
            if (libInputStream.read(classBytes) != classBytes.length) {
                throw new IOException("No....");
            }

            return defineClass(name, classBytes, 0, classBytes.length);

        } catch (IOException e) {
            throw new ClassNotFoundException(e.getMessage(), e);
        }
    }
}
