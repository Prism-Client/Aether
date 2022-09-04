package net.prismclient.aether.ui.image

import net.prismclient.aether.core.Aether
import net.prismclient.aether.ui.renderer.UIRenderer
import net.prismclient.aether.ui.resource.Resource
import net.prismclient.aether.ui.resource.ResourceProvider
import java.nio.ByteBuffer
import kotlin.math.max


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
    var imageBuffer: ByteBuffer
) : Resource {
    /**
     * When false, the dimensions when [retrieveImage] is called are multiplied by the
     * [Aether.devicePixelRatio] to support retina displays. To disable this, set this to true.
     */
    var ignoreDevicePx: Boolean = false

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
     * of [UIImage], the image might be resized to fit [width] and [height].
     */
    abstract fun retrieveImage(width: Float, height: Float): String
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
        Aether.renderer.deleteImage(imageName)
        ResourceProvider.deleteImage(imageName)
        ImageProvider.deleteImage(imageName)
        resizedImages?.forEach(ResizedImage::deallocate)
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
    override fun retrieveImage(width: Float, height: Float): String {
        val w = width * if (!ignoreDevicePx) Aether.instance.devicePixelRatio else 1f
        val h = height * if (!ignoreDevicePx) Aether.instance.devicePixelRatio else 1f

        // Resize the texture if not using MipMaps.
        return if ((w != initialWidth || h != initialHeight) && (imageFlags and GENERATE_MIPMAPS) == 0 && !disableScaling) {
            val expectedImage = "${imageName}_${w}x${h}"

            // If the texture does not already exist, generate it.
            if (!ImageProvider.images.contains(expectedImage)) {
                // Allocate resizeImages if necessary and resize the image
                resizedImages = resizedImages ?: arrayListOf()
                val resizedImage = Aether.renderer.resizeImage(
                    imageBuffer,
                    initialWidth.toInt(), initialHeight.toInt(),
                    w.toInt(), h.toInt()
                )

                // Register the newly generated image.
                // todo: change flags
                Aether.renderer.registerImage(expectedImage, w, h, REPEATX or REPEATY or PREMULTIPLIED, resizedImage)

                resizedImages!!.add(ResizedImage(expectedImage, resizedImage))
            }

            expectedImage
        } else imageName
    }

    /**
     * Represents a resized image.
     */
    class ResizedImage(val imageName: String, val imageBuffer: ByteBuffer) {
        /**
         * The time of which this resized image was created
         */
        val creationDate: Long = System.currentTimeMillis() // TODO: Automatically deallocate images

        fun deallocate() {
            Aether.renderer.deleteImage(imageName)
            ResourceProvider.deleteImage(imageName)
        }
    }
}

/**
 * Represents an SVG which is considered a type of image. The [imageBuffer] represents
 * the svg image, and [svgBuffer] represents the actual SVG data.
 *
 * @author sen
 * @since 1.0
 */
class SVG(
    imageName: String,
    initialWidth: Float,
    initialHeight: Float,

    /**
     * The scale of the SVG relative to the size set within it.
     */
    val initialScale: Float,
    val svgBuffer: ByteBuffer,
    imageBuffer: ByteBuffer
) : UIImage(imageName, initialWidth, initialHeight, imageBuffer) {
    var actualWidth: Float = initialWidth
    var actualHeight: Float = initialHeight

    /**
     * If the SVG is requested with a wanted size larger than the scale, the SVG will be
     * reloaded with the new scale. By default, this reflects the initial.
     */
    var svgScale: Float = initialScale

    override fun create() {
//        imageBuffer = Aether.renderer.ras
        ResourceProvider.registerImage(imageName, imageBuffer)
        Aether.renderer.registerImage(
            imageName,
            actualWidth * svgScale, actualHeight * svgScale,
            REPEATX or REPEATY or GENERATE_MIPMAPS, imageBuffer
        )
    }

    override fun deallocate() {
        Aether.renderer.deleteImage(imageName)
        ResourceProvider.deleteImage(imageName)
        ImageProvider.deleteImage(imageName)
    }

    /**
     * Return the actual reference of the SVG as long as the [width] and [height] are less than
     * the actual size times the [initialScale]. If it is larger than reallocate the image to fit the new size.
     */
    override fun retrieveImage(width: Float, height: Float): String {
        val w = width * if (!ignoreDevicePx) Aether.instance.devicePixelRatio else 1f
        val h = height * if (!ignoreDevicePx) Aether.instance.devicePixelRatio else 1f

        if (w > actualWidth * svgScale || h > actualHeight * svgScale) {
            val newScale = max(w / actualWidth.coerceAtLeast(1f), h / actualHeight.coerceAtLeast(1f))

            val image = Aether.renderer.rasterizeSVG(svgBuffer, newScale)

            imageBuffer = image.buffer
            actualWidth = image.width
            actualHeight = image.height
            svgScale = newScale

            ResourceProvider.registerImage(imageName, imageBuffer)
            Aether.renderer.registerImage(
                imageName,
                actualWidth * svgScale, actualHeight * svgScale,
                REPEATX or REPEATY or GENERATE_MIPMAPS, imageBuffer
            )
        }
        return imageName
    }
}