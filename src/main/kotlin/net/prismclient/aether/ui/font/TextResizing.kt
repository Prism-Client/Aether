package net.prismclient.aether.ui.font

/**
 * The equivalent of the Resizing property for fonts in Figma. See the respected enums
 * for information on what it does.
 *
 * @author sen
 * @since 1.0
 */
enum class TextResizing {
    /**
     * Renders the string as a one line text where the width and height are the bounds of it. If
     * the text contains a line break, this will automatically be switched to [AutoHeight].
     */
    AutoWidth,

    /**
     * Like [AutoWidth], however it supports multi-line text. The bounds will adjust to the overall size of the text.
     */
    AutoHeight,

    /**
     * Creates a newline when the width of the text exceeds the size of this. The height
     * is adjusted to be at least the height of the text.
     */
    FixedSize,

    /**
     * Cuts the text off at the point where it exceeds the width of this, and append the string [UIFont.truncatedText]
     */
    TruncateText,

    /**
     * Automatically figures out which one is the best based on the properties of the font.
     */
    Auto
}