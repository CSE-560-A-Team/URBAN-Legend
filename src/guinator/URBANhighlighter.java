package guinator;

import java.awt.Color;
import java.awt.Font;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.lateralgm.joshedit.DefaultTokenMarker;
import org.lateralgm.joshedit.TokenMarker;

import assemblernator.Assembler;
import assemblernator.Instruction;

/**
 * @author Josh Ventura
 * @date Apr 8, 2012; 7:35:51 PM
 */
public class URBANhighlighter extends DefaultTokenMarker implements TokenMarker {
	/**
	 * Construct and add syntax rules.
	 */
	public URBANhighlighter() {
		super();
		Color idColor = new Color(128, 0, 128);
		schemes.add(new BlockDescriptor("Comment", "(?<=;)", "[\r\n]", true, false,
				(char) 0, new Color(165, 165, 165), Font.ITALIC));
		schemes.add(new BlockDescriptor("Funky Identifier", "[a-z_A-Z]([^+\\-\\*/,;:\\s]*)'", "(?=[+\\-\\*/,;:\\s])", true, false,
				(char) 0, idColor, Font.ITALIC));
		schemes.add(new BlockDescriptor("String", "'", "'", true, true, '\\',
				new Color(0, 0, 255), 0));

		KeywordSet kws = new KeywordSet("Instructions", new Color(0, 0, 128),
				Font.BOLD, false);
		for (Entry<String, Instruction> i : Assembler.instructions.entrySet()) {
			kws.words.add(i.getKey().toLowerCase());
		}
		
		tmKeywords.add(kws);

		kws = new KeywordSet("OperandKeywords", new Color(0, 0, 255), 0, false);
		for (String i : Assembler.keyWords) {
			kws.words.add(i.toLowerCase());
		}
		tmKeywords.add(kws);

		CharSymbolSet css = new CharSymbolSet("Operators and Separators",
				new Color(255, 0, 0), 0);
		char[] ca = "{[()]}!%^&*-/+=?:~<>.,;".toCharArray();
		for (int i = 0; i < ca.length; i++)
			css.chars.add(ca[i]);
		tmChars.add(css);

		otherTokens.add(new SimpleToken("Numeric literal", "[0-9]+[FfUuLlDd]*",
				0, new Color(0, 225, 175)));

		default_kws = new KeywordSet("Label", idColor,
				Font.ITALIC);
		identifier_pattern = Pattern.compile("[a-z_A-Z]([^+\\-\\*/,;:\\s']*)");
	}
}