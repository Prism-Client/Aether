package net.prismclient.aether.ui.font

import net.prismclient.aether.core.util.extensions.safeByteBuffer
import java.nio.ByteBuffer

/**
 * Represents a family of fonts.
 *
 * @author sen
 * @since 1.0
 */
class UIFontFamily(
    val familyName: String,
    val thin: Font?,
    val extraLight: Font?,
    val light: Font?,
    val regular: Font?,
    val medium: Font?,
    val semiBold: Font?,
    val bold: Font?,
    val extraBold: Font?,
    val black: Font?
) {
    // TODO: Improve this
    // TODO: Dont allocate if it alreasdy exists within the UIResourceProvider

    constructor(
        familyName: String,
        thin: ByteBuffer?,
        extraLight: ByteBuffer?,
        light: ByteBuffer?,
        regular: ByteBuffer?,
        medium: ByteBuffer?,
        semiBold: ByteBuffer?,
        bold: ByteBuffer?,
        extraBold: ByteBuffer?,
        black: ByteBuffer?
    ) : this(
        familyName,
        if (thin != null) Font("$familyName-thin", thin) else null,
        if (extraLight != null) Font("$familyName-extralight", extraLight) else null,
        if (light != null) Font("$familyName-light", light) else null,
        if (regular != null) Font("$familyName-regular", regular) else null,
        if (medium != null) Font("$familyName-medium", medium) else null,
        if (semiBold != null) Font("$familyName-semibold", semiBold) else null,
        if (bold != null) Font("$familyName-bold", bold) else null,
        if (extraBold != null) Font("$familyName-extrabold", extraBold) else null,
        if (black != null) Font("$familyName-black", black) else null
    )

    constructor(familyName: String, location: String) : this(
        familyName,
        "$location/$familyName-thin.ttf".safeByteBuffer(),
        "$location/$familyName-extralight.ttf".safeByteBuffer(),
        "$location/$familyName-light.ttf".safeByteBuffer(),
        "$location/$familyName-regular.ttf".safeByteBuffer(),
        "$location/$familyName-medium.ttf".safeByteBuffer(),
        "$location/$familyName-semibold.ttf".safeByteBuffer(),
        "$location/$familyName-bold.ttf".safeByteBuffer(),
        "$location/$familyName-extrabold.ttf".safeByteBuffer(),
        "$location/$familyName-black.ttf".safeByteBuffer()
    )

    /**
     * Represents a single font with its memory, and it's [name].
     *
     * @author sen
     * @since 1.0
     */
    class Font(val name: String, val buffer: ByteBuffer)
}