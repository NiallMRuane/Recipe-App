package persistence

import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileReader
import java.io.FileWriter

/**
 * YAMLSerializer class for reading and writing objects to YAML files.
 *
 * @param file The file to read from or write to.
 */
class YAMLSerializer(private val file: File) : Serializer {

    /**
     * Reads and returns an object from the YAML file.
     *
     * @return The read object.
     * @throws Exception If an error occurs during reading.
     */
    @Throws(Exception::class)
    override fun read(): Any {
        val yaml = Yaml()
        // (exclusive to XStream) allowTypes(arrayOf(Note::class.java))
        // In YAML allowed types to not need to specified
        val read = FileReader(file)
        val obj = yaml.load(read) as Any
        read.close()
        return obj
    }

    /**
     * Writes the given object to the YAML file.
     *
     * @param obj The object to be written.
     * @throws Exception If an error occurs during writing.
     */
    @Throws(Exception::class)
    override fun write(obj: Any?) {
        val yaml = Yaml()
        val outputStream = FileWriter(file)
        yaml.dump(obj, outputStream)
        outputStream.close()
    }
}
