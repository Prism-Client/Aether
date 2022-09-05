package net.prismclient.aether.ui.dsl

import net.prismclient.aether.core.debug.warn
import net.prismclient.aether.core.util.extensions.byteBuffer
import net.prismclient.aether.core.util.extensions.toByteBuffer
import net.prismclient.aether.core.util.shorthands.Block
import net.prismclient.aether.ui.font.UIFontFamily
import net.prismclient.aether.ui.image.*
import net.prismclient.aether.ui.resource.Resource
import net.prismclient.aether.ui.resource.ResourceProvider
import org.apache.commons.io.FilenameUtils
import java.io.File
import java.nio.ByteBuffer

/**
 * [UIAssetDSL] provides utility functions for loading assets such as fonts and images.
 *
 * @author sen
 * @since 1.0
 */
object UIAssetDSL {
    /**
     * The default flags for when loading images
     */
    var IMAGE_FLAGS = REPEATX or REPEATY

    /**
     * Attempts to load an image from a resource location. If no flags are provided the default ones,
     * [IMAGE_FLAGS] will be applied. If the buffer is null, the returned value we be null.
     */
    @JvmStatic
    @JvmOverloads
    fun image(name: String, resourcePath: String, flags: Int = IMAGE_FLAGS): Image? =
        image(name, resourcePath.toByteBuffer(), flags)

    /**
     * Loads an image from the [buffer] and registers it with the [name]. If no flags are provided
     * the default ones, [IMAGE_FLAGS] will be applied. If the buffer is null, the returned value we
     * be null.
     */
    @JvmStatic
    @JvmOverloads
    fun image(name: String, buffer: ByteBuffer?, flags: Int = IMAGE_FLAGS): Image? = if (buffer == null) {
        warn("Failed to load image $name as the buffer was null. (File not found?)")
        null
    } else ImageProvider.createImage(name, flags, buffer)

    /**
     * Loads an SVG from a resource location, and registers it with the given [name].
     */
    @JvmStatic
    fun svg(name: String, resourcePath: String): SVG? = svg(name, resourcePath.toByteBuffer())

    /**
     * Loads an SVG from the [buffer] and registers it with the given [name].
     */
    @JvmStatic
    fun svg(name: String, buffer: ByteBuffer?): SVG? = if (buffer == null) {
        warn("Failed to load SVG $name as the buffer was null. (File not found?)")
        null
    } else ImageProvider.createSVG(name, buffer)

    /**
     * A utility function which expects a high order function, [action], a [location], and  target:
     * [fileExtensions]. With that information, the function will invoke [action] for each file that
     * ends with one of the values within [fileExtensions]. Moreover, a [prefix] will be appended along
     * with the file location, and the [suffix] will also be appended afterwards.
     *
     * @param deepSearch If true, folders within the given [location] will be walked through.
     * @return The number of files that were successfully loaded.
     */
    @JvmStatic
    @JvmOverloads
    fun heapLoad(
        location: File,
        prefix: String = "",
        suffix: String = "",
        fileExtensions: Array<String>,
        deepSearch: Boolean = true,
        action: (fileName: String, fileExtension: String, fileData: ByteBuffer) -> Resource?
    ): Int {
        if (!location.exists()) {
            warn("Failed to heap load from $location as it does not exist.")
            return 0
        }

        var count = 0

        for (
        file in location.listFiles() ?: run {
            warn("Given file location does not yield a list of files")
            return 0
        }
        ) {
            val extension = FilenameUtils.getExtension(file.name).lowercase()

            if (file.isDirectory) {
                heapLoad(file, prefix + "${file.name}/", suffix, fileExtensions, deepSearch, action)
            } else if (fileExtensions.isEmpty() || fileExtensions.any(extension::equals)) {
                if (action.invoke(
                        prefix + file.nameWithoutExtension + suffix,
                        extension,
                        file.inputStream().byteBuffer(),
                    ) != null
                ) {
                    println("Loaded file: ${prefix + file.nameWithoutExtension + suffix}")
                    count++
                }
            }
        }
        return count
    }

    /**
     * Creates a [File] from a local resource.
     */
    @JvmStatic
    fun localResource(location: String): File = File(UIAssetDSL::class.java.getResource(location)!!.toURI())

    /**
     * Loads all the PNGs within the given [location] and sub-locations.
     */
    fun pngCollection(location: File, prefix: String = ""): Int = heapLoad(
        location = location,
        prefix = prefix,
        fileExtensions = arrayOf("png"),
    ) { name, _, buffer -> image(name, buffer) }

    /**
     * Loads all the SVGs within the given [location] and sub-locations.
     */
    @JvmStatic
    @JvmOverloads
    fun svgCollection(location: File, prefix: String = ""): Int = heapLoad(
        location = location,
        prefix = prefix,
        fileExtensions = arrayOf("svg")
    ) { name, _, buffer -> svg(name, buffer) }

    /**
     * Loads a folder of fonts with their file name as the registered name.
     *
     * @see fontFamily
     */
    @JvmStatic
    fun fontCollection(location: File) = heapLoad(
        location = location,
        fileExtensions = arrayOf("ttf"),
        deepSearch = false
    ) { fileName, _, fileData -> ResourceProvider.registerFont(fileName, fileData) }

    /**
     * Like [fontCollection], except it is loaded into a [UIFontFamily].
     */
    @JvmStatic
    fun fontFamily(): UIFontFamily = TODO("Font family bulk loading.")
}

/**
 * Creates DSL block of [UIAssetDSL].
 */
inline fun resource(block: Block<UIAssetDSL>) = UIAssetDSL.block()