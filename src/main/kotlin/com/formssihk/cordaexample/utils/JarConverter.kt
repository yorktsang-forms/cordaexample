package com.formssihk.cordaexample.utils

import com.google.common.io.ByteStreams
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.jar.JarEntry
import java.util.jar.JarInputStream
import java.util.jar.JarOutputStream


object JarConverter1 {

    class TransferrableBAOStream(size: Int) : ByteArrayOutputStream(size) {
        fun stealBuf(): ByteArrayInputStream {
            val result = ByteArrayInputStream(this.buf, 0, this.count)
            this.buf = ByteArray(1)
            this.count = 0
            this.close()
            return result
        }
    }

    fun createJar(data: InputStream): InputStream {
        val buf = TransferrableBAOStream(0)
        val jar = JarOutputStream(buf)

        val entry = JarEntry("document.pdf")
        // saving size does not work
        jar.putNextEntry(entry)
        ByteStreams.copy(data, jar)

        jar.closeEntry()
        jar.close()

        return buf.stealBuf()
    }

    fun extractDocument(jarData: InputStream, dest: OutputStream) {
        val jar = JarInputStream(jarData)
        jar.use {
            val entry = it.nextJarEntry ?: throw IllegalArgumentException("JAR file contains no files")
            ByteStreams.copy(it, dest)
            it.closeEntry()
        }
    }
}