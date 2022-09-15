package net.prismclient.aether.ui.component.type

import net.prismclient.aether.core.animation.AnimationContext
import net.prismclient.aether.core.color.UIColor
import net.prismclient.aether.core.util.shorthands.*
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.dsl.Renderer
import net.prismclient.aether.ui.image.ImageProvider
import net.prismclient.aether.ui.image.UIImage
import net.prismclient.aether.ui.modifier.UIModifier
import java.io.FileNotFoundException

/**
 * A type of [UIComponent] which renders an image.
 *
 * @author sen
 * @since 1.0
 */
class ImageComponent(image: UIImage, modifier: IconModifier) : UIComponent<ImageComponent>(modifier) {
    override val modifier: IconModifier = super.modifier as IconModifier

    var image: UIImage = image
        set(value) {
            field = value
            if (width > 0f && height > 0f)
                imageHandle = image.retrieveImage(width, height)
        }

    private var imageHandle: String = ""

    /**
     * Attempts to create an image from the [imageName]. A NPE will
     * be thrown if the image is not found within [ImageProvider].
     */
    constructor(imageName: String, modifier: IconModifier) : this(
        ImageProvider.obtainImage(imageName)
            ?: throw FileNotFoundException("Image $imageName was not found. Was it registered?"),
        modifier
    )

    override fun update() {
        imageHandle = image.retrieveImage(width, height)
    }


    override fun renderComponent() = Renderer {
        color(modifier.imageColor)
        renderImage(imageHandle, x, y, width, height)
    }

    override fun copy(): ImageComponent =
        ImageComponent(image, modifier.copy())
}

class IconModifier : UIModifier<IconModifier>() {
    var imageColor: UIColor? = null

    override fun copy(): IconModifier = IconModifier().also {
        it.x = x.copy
        it.y = y.copy
        it.width = width.copy
        it.height = height.copy
        it.anchorPoint = anchorPoint.copy
        it.padding = padding.copy
        it.margin = margin.copy
        it.opacity = opacity.copy
        it.background = background.copy
        it.imageColor = imageColor.copy
    }

    override fun merge(other: IconModifier?) {
        if (other != null) {
            x = other.x or x
            y = other.y or y
            width = other.width or width
            height = other.height or height
            anchorPoint = other.anchorPoint or anchorPoint
            padding = other.padding or padding
            margin = other.margin or margin
            opacity = other.opacity or opacity
            background = other.background or background
            imageColor = other.imageColor or imageColor
        }
    }

    override fun animate(context: AnimationContext<*>, initial: IconModifier?, start: IconModifier?, end: IconModifier?, progress: Float) {
        TODO("Not yet implemented")
    }
}

fun IconModifier.imageColor(color: UIColor) = apply {
    imageColor = color
}