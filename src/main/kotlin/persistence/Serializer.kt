package persistence

/**
 * Serializer interface for defining methods to read and write objects.
 */
interface Serializer {

    /**
     * Writes the given object.
     *
     * @param obj The object to be written.
     * @throws Exception If an error occurs during writing.
     */
    @Throws(Exception::class)
    fun write(obj: Any?)

    /**
     * Reads and returns an object.
     *
     * @return The read object.
     * @throws Exception If an error occurs during reading.
     */
    @Throws(Exception::class)
    fun read(): Any?
}