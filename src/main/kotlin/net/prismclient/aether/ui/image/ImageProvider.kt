package net.prismclient.aether.ui.image

import net.prismclient.aether.core.Aether
import net.prismclient.aether.ui.resource.ResourceProvider
import java.nio.ByteBuffer

/**
 * [ImageProvider] works in unison with [ResourceProvider] to register, load, resize and rasterize
 * images.
 *
 * @author sen
 * @since 1.0
 */
object ImageProvider {
    @JvmStatic
    val images: HashMap<String, ByteBuffer> = ResourceProvider.images

    /**
     * Creates a new [Image], a common subtype of [UIImage] which is intended to support general
     * image file types, such as JPEG and PNG. [Image.create] is automatically invoked and registered
     * to [ResourceProvider].
     */
    @JvmStatic
    fun createImage(imageName: String, flags: Int, buffer: ByteBuffer): Image {
        val image = Aether.renderer.createImage(buffer)
        return Image(imageName, image.width, image.height, flags, image.buffer).also(UIImage::create)
    }

    /**
     * Creates a new [SVG], a subtype of [UIImage] which is intended to support SVGs. [SVG.create] is
     * automatically invoked and registered to [ResourceProvider].
     */
    @JvmStatic
    @JvmOverloads
    fun createSVG(svgName: String, buffer: ByteBuffer, scale: Float = 1f): SVG {
        val svg = Aether.renderer.rasterizeSVG(buffer, scale)
        return SVG(svgName, svg.width, svg.height, scale, buffer, svg.buffer).also(UIImage::create)
    }
}