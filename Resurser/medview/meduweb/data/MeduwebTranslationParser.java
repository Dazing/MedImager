/*
 * @(#)MeduwebTranslationParser.java
 *
 * $Id: MeduwebTranslationParser.java,v 1.1 2003/07/21 21:55:08 d99figge Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.meduweb.data;

import medview.common.generator.*;
import medview.common.translator.*;

public class MeduwebTranslationParser implements TranslationConstants
{
	public String parse(String translation, String self, MeduwebTranslationModel model)
	{
		if (containsNoline(translation)) { return null; }

		String ret = parseSelves(translation, self, model);

		return applyVG(ret, model);
	}

	private boolean containsNoline(String translation)
	{
		return (translation.indexOf(TRANSLATION_NOLINE) != -1);
	}

	private String parseSelves(String translation, String self, MeduwebTranslationModel model)
	{
		String parsed = translation;

		String preSelf, postSelf;

		int selfIndex = parsed.indexOf(TRANSLATION_ITSELF);

		while (selfIndex != -1)
		{
			preSelf = parsed.substring(0, selfIndex);

			postSelf = parsed.substring(selfIndex + selfLength);

			parsed = preSelf + applyVG(self, model) + postSelf;

			selfIndex = parsed.indexOf(TRANSLATION_ITSELF);
		}

		return parsed;
	}

	private String applyVG(String word, MeduwebTranslationModel model)
	{
		if (model.isAutoVG())
		{
			switch (model.getVGPolicy())
			{
				case TranslationModel.AUTO_VG_POLICY_GEMEN:
				{
					return GeneratorUtilities.gemenize(word);
				}

				case TranslationModel.AUTO_VG_POLICY_VERSAL:
				{
					return GeneratorUtilities.versalize(word);
				}

				default:
				{
					System.out.println("Warning - unrecognized VG policy" +

									   "detected (" + model.getVGPolicy() +

									   ") - will return the word unparsed...");
					return word;
				}
			}
		}
		else
		{
			return word;
		}
	}

	public static MeduwebTranslationParser instance()
	{
		if (instance == null) { instance = new MeduwebTranslationParser(); }

		return instance;
	}

	private MeduwebTranslationParser()
	{
		selfLength = TRANSLATION_ITSELF.length();
	}

	private static MeduwebTranslationParser instance;

	private int selfLength;
}