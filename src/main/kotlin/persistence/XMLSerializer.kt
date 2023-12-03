package persistence

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.xml.DomDriver
import models.Recipe
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.lang.Exception
import kotlin.Throws

/**
 * XMLSerializer class for reading and writing objects to XML files.
 *
 * @param file The file to read from or write to.
 */
class XMLSerializer(private val file: File) : Serializer {

    /**
     * Reads and returns an object from the XML file.
     *
     * @return The read object.
     * @throws Exception If an error occurs during reading.
     */
    @Throws(Exception::class)
    override fun read(): Any {
        val xStream = XStream(DomDriver())
        xStream.allowTypes(arrayOf(Recipe::class.java))
        val inputStream = xStream.createObjectInputStream(FileReader(file))
        val obj = inputStream.readObject() as Any
        inputStream.close()
        return obj
    }

    /**
     * Writes the given object to the XML file.
     *
     * @param obj The object to be written.
     * @throws Exception If an error occurs during writing.
     */
    @Throws(Exception::class)
    override fun write(obj: Any?) {
        val xStream = XStream(DomDriver())
        val outputStream = xStream.createObjectOutputStream(FileWriter(file))
        outputStream.writeObject(obj)
        outputStream.close()
    }
}
