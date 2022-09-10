package net.prismclient.aether.ui.component.type

import net.prismclient.aether.core.color.UIAlpha
import net.prismclient.aether.core.color.UIColor
import net.prismclient.aether.core.util.shorthands.*
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.composition.util.UIBackground
import net.prismclient.aether.ui.dsl.Renderer
import net.prismclient.aether.ui.image.ImageProvider
import net.prismclient.aether.ui.image.UIImage
import net.prismclient.aether.ui.modifier.UIModifier
import net.prismclient.aether.ui.unit.other.AnchorPoint
import net.prismclient.aether.ui.unit.other.Margin
import net.prismclient.aether.ui.unit.other.Padding
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
        ImageComponent(image, modifier.copy)
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

    override fun animate(start: IconModifier?, end: IconModifier?, fraction: Float) {
        ifNotNull(start?.x, end?.x) {
            x = x ?: 0.px
            x!!.lerp(x, start?.x, end?.x, fraction)
        }
        ifNotNull(start?.y, end?.y) {
            y = y ?: 0.px
            y!!.lerp(y, start?.y, end?.y, fraction)
        }
        ifNotNull(start?.width, end?.width) {
            width = width ?: 0.px
            width!!.lerp(width, start?.width, end?.width, fraction)
        }
        ifNotNull(start?.height, end?.height) {
            height = height ?: 0.px
            height!!.lerp(height, start?.height, end?.height, fraction)
        }
        ifNotNull(start?.anchorPoint, end?.anchorPoint) {
            anchorPoint = anchorPoint ?: AnchorPoint()
            anchorPoint!!.animate(start?.anchorPoint, end?.anchorPoint, fraction)
        }
        ifNotNull(start?.padding, end?.padding) {
            padding = padding ?: Padding(null, null, null, null)
            padding!!.animate(start?.padding, end?.padding, fraction)
        }
        ifNotNull(start?.margin, end?.margin) {
            margin = margin ?: Margin(null, null, null, null)
            margin!!.animate(start?.margin, end?.margin, fraction)
        }
        ifNotNull(start?.opacity, end?.opacity) {
            opacity = opacity ?: UIAlpha(1f)
            opacity!!.value = lerp(start?.opacity?.value ?: 1f, end?.opacity?.value ?: 1f, fraction)
        }
        ifNotNull(start?.background, end?.background) {
            background = background ?: UIBackground()
            background!!.animate(start?.background, end?.background, fraction)
        }
        ifNotNull(start?.imageColor, end?.imageColor) {
            imageColor = imageColor ?: UIColor(-1)
            imageColor!!.animate(start?.imageColor, end?.imageColor, fraction)
        }
    }
}

fun IconModifier.imageColor(color: UIColor) = apply {
    imageColor = color
}