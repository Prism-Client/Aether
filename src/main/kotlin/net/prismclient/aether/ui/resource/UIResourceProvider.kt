package net.prismclient.aether.ui.resource

import net.prismclient.aether.ui.font.UIFontFamily
import net.prismclient.aether.ui.image.UIImageData

/**
 * [UIResourceProvider] manages fonts and images for Aether.
 *
 * @author sen
 * @since 1.0
 */
object UIResourceProvider {
    val fonts: HashMap<String, UIFontFamily> = hashMapOf()
    val images: HashMap<String, UIImageData> = hashMapOf()

    fun registerFontFamily(family: UIFontFamily) {
        fonts[family.familyName] = family
    }

    fun registerImage(imageName: String, image: UIImageData) {
        images[imageName] = image
    }
}