package net.prismclient.aether.ui.resource

import net.prismclient.aether.ui.font.UIFontFamily
import net.prismclient.aether.ui.image.ImageProvider
import java.nio.ByteBuffer

/**
 * [ResourceProvider] manages fonts and images for Aether.
 *
 * @author sen
 * @since 1.0
 */
object ResourceProvider {
    val images: HashMap<String, ByteBuffer> = hashMapOf()

    val fonts: HashMap<String, UIFontFamily.Font> = hashMapOf()
    val fontFamilies: HashMap<String, UIFontFamily> = hashMapOf()

    fun registerFontFamily(family: UIFontFamily) {
        fontFamilies[family.familyName] = family
    }

    fun registerFont(fontName: String, fontData: ByteBuffer) {
        registerFont(UIFontFamily.Font(fontName, fontData))
    }

    fun registerFont(font: UIFontFamily.Font) {
        fonts[font.name] = font
    }

    /**
     * Registers the image handle, [imageName] to the [images] HashMap with [image] as the value.
     *
     * For creating images use [ImageProvider.createImage] or [ImageProvider.createSVG] for creating images.
     */
    fun registerImage(imageName: String, image: ByteBuffer) {
        images[imageName] = image
    }
}