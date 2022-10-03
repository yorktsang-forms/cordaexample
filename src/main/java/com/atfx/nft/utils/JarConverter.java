package com.atfx.nft.utils;

import com.atfx.nft.service.impl.DefaultCordaService;
import com.google.common.io.ByteStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;

public class JarConverter {
    private static Logger log = LoggerFactory.getLogger(DefaultCordaService.class);
    static class TransferrableBAOStream extends ByteArrayOutputStream {

        TransferrableBAOStream(Integer size) {
            super(size);
        }

        ByteArrayInputStream stealBuf() throws IOException {
            ByteArrayInputStream result = new ByteArrayInputStream(this.buf, 0, this.count);
            this.buf = new byte[1];
            this.count = 0;
            this.close();
            return result;
        }
    }

    public static InputStream createJar(InputStream data) {

        try {
            TransferrableBAOStream buf = new TransferrableBAOStream(0);
            JarOutputStream jar = new JarOutputStream(buf);

            JarEntry entry = new JarEntry("document.pdf");
            jar.putNextEntry(entry);
            ByteStreams.copy(data, jar);

            jar.closeEntry();
            jar.close();

            return buf.stealBuf();
        } catch (Exception e) {
            log.error("Error occur when creating jar", e);
        }
        return null;
    }

    public static void extractDocument(InputStream jarData, OutputStream dest) {
        try {
            JarInputStream jar = new JarInputStream(jarData);

            if (jar.getNextEntry() !=  null) {
                ByteStreams.copy(jar, dest);
                jar.closeEntry();
            } else {
                throw new IllegalArgumentException("JAR file contains no files");
            }
        } catch (Exception e) {
            log.error("Error occur when extracting document", e);
        }
    }
}
