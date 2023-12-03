package utils

import java.lang.NumberFormatException
import java.util.Scanner

/**
 * Utility class for handling user input using Scanner.
 */
object ScannerInput {

    /**
     * Reads the next integer from the user input.
     *
     * @param prompt The prompt to display to the user.
     * @return The next integer entered by the user.
     */
    @JvmStatic
    fun readNextInt(prompt: String?): Int {
        do {
            try {
                print(prompt)
                return Scanner(System.`in`).next().toInt()
            } catch (e: NumberFormatException) {
                System.err.println("\tEnter a number please.")
            }
        } while (true)
    }

    /**
     * Reads the next double from the user input.
     *
     * @param prompt The prompt to display to the user.
     * @return The next double entered by the user.
     */
    @JvmStatic
    fun readNextDouble(prompt: String?): Double {
        do {
            try {
                print(prompt)
                return Scanner(System.`in`).next().toDouble()
            } catch (e: NumberFormatException) {
                System.err.println("\tEnter a number please.")
            }
        } while (true)
    }

    /**
     * Reads the next line (string) from the user input.
     *
     * @param prompt The prompt to display to the user.
     * @return The next line (string) entered by the user.
     */
    @JvmStatic
    fun readNextLine(prompt: String?): String {
        print(prompt)
        return Scanner(System.`in`).nextLine()
    }

    /**
     * Reads the next character from the user input.
     *
     * @param prompt The prompt to display to the user.
     * @return The next character entered by the user.
     */
    @JvmStatic
    fun readNextChar(prompt: String?): Char {
        print(prompt)
        return Scanner(System.`in`).next()[0]
    }

    /**
     * Reads the next boolean from the user input.
     *
     * @param prompt The prompt to display to the user.
     * @return The next boolean entered by the user.
     */
    @JvmStatic
    fun readNextBoolean(prompt: String?): Boolean {
        print(prompt)
        return Scanner(System.`in`).nextBoolean()
    }
}
