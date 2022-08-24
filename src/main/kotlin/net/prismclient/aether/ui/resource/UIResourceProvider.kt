package net.prismclient.aether.ui.resource

import net.prismclient.aether.ui.font.Font
import net.prismclient.aether.ui.font.UIFont
import net.prismclient.aether.ui.font.UIFontFamily
import net.prismclient.aether.ui.image.UIImageData
import java.nio.ByteBuffer

/**
 * [UIResourceProvider] manages fonts and images for Aether.
 *
 * @author sen
 * @since 1.0
 */
object UIResourceProvider {
    val fonts: HashMap<String, UIFontFamily.Font> = hashMapOf()
    val fontFamilies: HashMap<String, UIFontFamily> = hashMapOf()
    val images: HashMap<String, UIImageData> = hashMapOf()

    fun registerFontFamily(family: UIFontFamily) {
        fontFamilies[family.familyName] = family
    }

    fun registerFont(fontName: String, fontData: ByteBuffer) {
        registerFont(UIFontFamily.Font(fontName, fontData))
    }

    fun registerFont(font: UIFontFamily.Font) {
        fonts[font.name] = font
    }

    fun registerImage(imageName: String, image: UIImageData) {
        images[imageName] = image
    }
}