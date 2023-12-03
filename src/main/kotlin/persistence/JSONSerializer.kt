package persistence

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver
import models.Recipe
import java.io.File
import java.io.FileReader
import java.io.FileWriter

/**
 * JSONSerializer class for serializing and deserializing Recipe objects to and from JSON.
 *
 * @property file The File object representing the file to read from or write to.
 */
class JSONSerializer(private val file: File) : Serializer {

    /**
     * Reads the data from the JSON file and deserializes it into an object.
     *
     * @return The deserialized object.
     * @throws Exception If an error occurs during reading or deserialization.
     */
    @Throws(Exception::class)
    override fun read(): Any {
        val xStream = XStream(JettisonMappedXmlDriver())
        xStream.allowTypes(arrayOf(Recipe::class.java))
        val inputStream = xStream.createObjectInputStream(FileReader(file))
        val obj = inputStream.readObject() as Any
        inputStream.close()
        return obj
    }

    /**
     * Writes the given object to the JSON file after serializing it.
     *
     * @param obj The object to be serialized and written to the file.
     * @throws Exception If an error occurs during serialization or writing.
     */
    @Throws(Exception::class)
    override fun write(obj: Any?) {
        val xStream = XStream(JettisonMappedXmlDriver())
        val outputStream = xStream.createObjectOutputStream(FileWriter(file))
        outputStream.writeObject(obj)
        outputStream.close()
    }
}