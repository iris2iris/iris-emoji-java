package com.vdurmont.emoji;

import java.util.List;

/**
 * This class represents an emoji.<br>
 * <br>
 * This object is immutable so it can be used safely in a multithreaded context.
 *
 * @author Improver: Ivan Ivanov [https://vk.com/irisism]<br>
 * Creator: Vincent DURMONT [vdurmont@gmail.com]
 */
public class Emoji {
	public final String description;
	public final boolean supportsFitzpatrick;
	public final int sequenceType;
	public final List<String> aliases;
	public final List<String> tags;
	public final String unicode;
	public final String htmlDec;
	public final String htmlHex;
	public final String emojiChar;

	public static final int SEQUENCE_BASE_SKIN_GENDER = 1;
	public static final int SEQUENCE_GENDER_SKIN_BASE = 2;

	/**
	 * Constructor for the Emoji.
	 *
	 * @param description		The description of the emoji
	 * @param sequenceType		Whether the emoji supports Fitzpatrick modifiers
	 * @param aliases			the aliases for this emoji
	 * @param tags				the tags associated with this emoji
	 * @param unicode				the bytes that represent the emoji
	 */
	protected Emoji(
			String description,
			int sequenceType,
			List<String> aliases,
			List<String> tags,
			String unicode,
			String emojiChar
	) {
		this.description = description;
		this.sequenceType = sequenceType;
		this.supportsFitzpatrick = sequenceType != 0;
		this.aliases = aliases;
		this.tags = tags;

		int count = 0;
		this.unicode = unicode;
		this.emojiChar = emojiChar;
		int stringLength = unicode.length();
		String[] pointCodes = new String[stringLength];
		String[] pointCodesHex = new String[stringLength];

		for (int offset = 0; offset < stringLength; ) {
			final int codePoint = getUnicode().codePointAt(offset);

			pointCodes[count] = String.format("&#%d;", codePoint);
			pointCodesHex[count++] = String.format("&#x%x;", codePoint);

			offset += Character.charCount(codePoint);
		}
		this.htmlDec = stringJoin(pointCodes, count);
		this.htmlHex = stringJoin(pointCodesHex, count);
	}

	/**
	 * Method to replace String.join, since it was only introduced in java8
	 *
	 * @param array the array to be concatenated
	 * @return concatenated String
	 */
	private String stringJoin(String[] array, int count) {
		StringBuilder joined = new StringBuilder();
		for (int i = 0; i < count; i++)
			joined.append(array[i]);
		return joined.toString();
	}

	/**
	 * Returns the description of the emoji
	 *
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Returns wether the emoji supports the Fitzpatrick modifiers or not
	 *
	 * @return true if the emoji supports the Fitzpatrick modifiers
	 */
	public boolean supportsFitzpatrick() {
		return this.supportsFitzpatrick;
	}

	/**
	 * Returns the aliases of the emoji
	 *
	 * @return the aliases (unmodifiable)
	 */
	public List<String> getAliases() {
		return this.aliases;
	}

	/**
	 * Returns the tags of the emoji
	 *
	 * @return the tags (unmodifiable)
	 */
	public List<String> getTags() {
		return this.tags;
	}

	/**
	 * Returns the unicode representation of the emoji
	 *
	 * @return the unicode representation
	 */
	public String getUnicode() {
		return this.unicode;
	}

	public String getEmojiChar() {return this.emojiChar; }

	/**
	 * Returns the unicode representation of the emoji associated with the
	 * provided Fitzpatrick modifier.<br>
	 * If the modifier is null, then the result is similar to
	 * {@link Emoji#getUnicode()}
	 *
	 * @param fitzpatrick the fitzpatrick modifier or null
	 * @return the unicode representation
	 * @throws UnsupportedOperationException if the emoji doesn't support the
	 *                                       Fitzpatrick modifiers
	 */
	public String getUnicode(Fitzpatrick fitzpatrick) {
		if (!this.supportsFitzpatrick()) {
			throw new UnsupportedOperationException(
					"Cannot get the unicode with a fitzpatrick modifier, " +
							"the emoji doesn't support fitzpatrick."
			);
		} else if (fitzpatrick == null) {
			return this.getUnicode();
		}
		return this.getUnicode() + fitzpatrick.unicode;
	}

	/**
	 * Returns the HTML decimal representation of the emoji
	 *
	 * @return the HTML decimal representation
	 */
	public String getHtmlDecimal() {
		return this.htmlDec;
	}

	/**
	 * Returns the HTML hexadecimal representation of the emoji
	 *
	 * @return the HTML hexadecimal representation
	 */
	public String getHtmlHexadecimal() {
		return this.htmlHex;
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof Emoji &&
			((Emoji) other).getUnicode().equals(getUnicode());
	}

	@Override
	public int hashCode() {
		return unicode.hashCode();
	}

	/**
	 * Returns the String representation of the Emoji object.<br>
	 * <br>
	 * Example:<br>
	 * <code>Emoji {
	 * description='smiling face with open mouth and smiling eyes',
	 * supportsFitzpatrick=false,
	 * aliases=[smile],
	 * tags=[happy, joy, pleased],
	 * unicode='😄',
	 * htmlDec='&amp;#128516;',
	 * htmlHex='&amp;#x1f604;'
	 * }</code>
	 *
	 * @return the string representation
	 */
	@Override
	public String toString() {
		return "Emoji{" +
				"description='" + description + '\'' +
				", supportsFitzpatrick=" + supportsFitzpatrick +
				", aliases=" + aliases +
				", tags=" + tags +
				", unicode='" + unicode + '\'' +
				", htmlDec='" + htmlDec + '\'' +
				", htmlHex='" + htmlHex + '\'' +
				'}';
	}
}
