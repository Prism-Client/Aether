package net.prismclient.aether.ui.font

/**
 * [FontType] also known as the resizing property for text in Figma is an important part of Fonts within Aether. It
 * determines how to format and render the text given to the font. See the enums of this for information on how this
 * works. Depending on the enum, the composable will become dynamic as it might resize the composable.
 *
 * @author sen
 * @since 1.0
 */
enum class FontType {
    /**
     * Dynamic. The given text is rendered as one line where the width grows based on the size of the text bounds.
     */
    AutoWidth,

    /**
     * Like [AutoWidth], as it's dynamic, however it supports multi-line text and will wrap anything that
     * exceeds [UIFont.width]. The height will grow to fit the bounds of the multi-line text.
     */
    AutoHeight,

    /**
     * [FixedSize] is not dynamic and does not change based on the content. Instead, any text which exceeds the
     * horizontal bounds (width), will be wrapped.
     */
    FixedSize,

    /**
     * Not yet implemented
     */
    TruncateText
}