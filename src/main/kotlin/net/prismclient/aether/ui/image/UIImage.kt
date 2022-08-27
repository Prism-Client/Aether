package net.prismclient.aether.ui.image

import net.prismclient.aether.core.Aether
import net.prismclient.aether.ui.renderer.UIRenderer
import net.prismclient.aether.ui.resource.ResourceProvider
import java.nio.ByteBuffer


/**
 * Represents an image loaded into memory. It contains metadata info which is useful when Aether
 * or an external program needs information about the image, such as the size or flags used when
 * initially loaded into Aether, as well as minified versions of the image.
 *
 * @author sen
 * @since 1.0
 */
abstract class UIImage(
    /**
     * The name of an image registered
     */
    var imageName: String,

    /**
     * The width of which the image is loaded at.
     */
    var initialWidth: Float,

    /**
     * The height of which the image is loaded at.
     */
    var initialHeight: Float,

    /**
     * The actual image data used when initially loaded. Depending on the renderer, the
     * image might need to be saved to avoid garbage collection.
     */
    val imageBuffer: ByteBuffer
) {
    /**
     * Creates this by invoking the necessary [UIRenderer] functions, and
     * registers this to [ResourceProvider] if necessary.
     */
    abstract fun create()

    /**
     * Deletes this from memory. Any references to this must also be removed to be fully eligible for garbage collection.
     */
    abstract fun deallocate()

    /**
     * Returns the string reference of this image which can be passed to [UIRenderer]. Depending on the type
     * of [UIImage], the image might be resized to fit [wantedWidth] and [wantedHeight].
     */
    abstract fun retrieveImage(wantedWidth: Float, wantedHeight: Float): String
}

// Image flags

// TODO: Document image flags

/**
 * Indicates to Aether that the [Image] should generate mipmaps. Note: a new raster
 * will not be created if the wanted size is not equal to the inital size when true.
 */
const val GENERATE_MIPMAPS = 1
const val REPEATX = 2
const val REPEATY = 4
const val FLIPY = 8
const val PREMULTIPLIED = 16
const val NEAREST = 32

/**
 * Represents a general image, such as a JPEG, or PNG. When the image is not equal to the actual
 * size a new texture is generated via [UIRenderer.resizeImage]. However, this will not happen
 * if [GENERATE_MIPMAPS] is passed as an image flags. Instead, the renderer will generate the MipMaps,
 * and this will return the normal reference.
 *
 * @author sen
 * @since 1.0
 */
class Image(
    imageName: String,
    initialWidth: Float,
    initialHeight: Float,
    val imageFlags: Int,
    imageBuffer: ByteBuffer
) : UIImage(imageName, initialWidth, initialHeight, imageBuffer) {
    /**
     * The resized images of this.
     *
     * @see ResizedImage
     */
    var resizedImages: ArrayList<ResizedImage>? = null

    /**
     * When true, Aether will not generate resized textures when asked.
     */
    var disableScaling: Boolean = false

    override fun create() {
        ResourceProvider.registerImage(imageName, imageBuffer)
        Aether.renderer.registerImage(imageName, initialWidth, initialHeight, imageFlags, imageBuffer)
    }

    override fun deallocate() {
    }

    /**
     * Return the image if the wanted size is equal to the actual size. If not this might generate a new image
     * based on the new wanted image and return it formatted as:
     *
     *      imageName_{wantedWidth}x${wantedHeight}
     *      Logo_64x64
     *
     * This does not happen if MipMaps are enabled with [imageFlags].
     */
    override fun retrieveImage(wantedWidth: Float, wantedHeight: Float): String {
        // Resize the texture if not using MipMaps.
        return if ((wantedWidth != initialWidth || wantedHeight != initialHeight) && !usesMipMaps() && !disableScaling) {
            val expectedImage = "${imageName}_${wantedWidth}x${wantedHeight}"

            // If the texture does not already exist, generate it.
            if (!ImageProvider.images.contains(expectedImage)) {
                // Allocate resizeImages if necessary and resize the image
                resizedImages = resizedImages ?: arrayListOf()
                val resizedImage = Aether.renderer.resizeImage(
                    imageBuffer,
                    initialWidth.toInt(), initialHeight.toInt(),
                    wantedWidth.toInt(), wantedHeight.toInt()
                )

                // Register the newly generated image.
                // todo: change flags
                Aether.renderer.registerImage(expectedImage, wantedWidth, wantedHeight, REPEATX or REPEATY or PREMULTIPLIED, resizedImage)

                resizedImages!!.add(ResizedImage(expectedImage, resizedImage))
            }

            expectedImage
        } else imageName
    }

    /**
     * Returns true if this image uses MipMaps.
     */
    fun usesMipMaps(): Boolean = (imageFlags and GENERATE_MIPMAPS) != 0

    /**
     * Represents a resized image.
     */
    class ResizedImage(val imageName: String, val imageBuffer: ByteBuffer) {
        /**
         * The time of which this resized image was created
         */
        val creationDate: Long = System.currentTimeMillis()
    }

    /**
     * Deallocates the given resized image from memory and from [resizedImages]
     */
    fun ResizedImage.deallocate() {
        TODO("Deallocate Resized Image")
        // Memfree...
    }
}

/**
 * Represents an SVG which is considered a type of image.
 *
 * @author sen
 * @since 1.0
 */
class SVG(
    imageName: String,
    actualWidth: Float,
    actualHeight: Float,

    /**
     * The scale of the SVG relative to the size set within it.
     */
    val initialScale: Float,
    imageBuffer: ByteBuffer
) : UIImage(imageName, actualWidth, actualHeight, imageBuffer) {
    /**
     * If the SVG is requested with a wanted size larger than the scale, the SVG will be
     * reloaded with the new scale. By default, this reflects the initial.
     */
    var resizedScale: Float = initialScale

    override fun create() {
        ResourceProvider.registerImage(imageName, imageBuffer)
        Aether.renderer.registerImage(imageName, initialWidth, initialHeight, REPEATX or REPEATY or GENERATE_MIPMAPS, imageBuffer)
    }

    override fun deallocate() {
        TODO("Not yet implemented")
    }

    /**
     * Return the actual reference of the SVG as long as the [wantedWidth] and [wantedHeight] are less than
     * the actual size times the [initialScale]. If it is larger than reallocate the image to fit the new size.
     */
    override fun retrieveImage(wantedWidth: Float, wantedHeight: Float): String {
        // If the wanted size exceeds the actual size times the resizedScale, re-rasterize the image.
        if (wantedWidth >= initialWidth * resizedScale || wantedHeight >= initialHeight * resizedScale) {

        }
        return imageName
    }
}