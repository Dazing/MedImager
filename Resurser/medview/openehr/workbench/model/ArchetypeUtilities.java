//
//  ArchetypeUtilities.java
//  WorkBench-GUI
//
//  Created by Olof Torgersson on 2008-12-13.
//
//  $Id: ArchetypeUtilities.java,v 1.5 2008/12/28 19:17:38 oloft Exp $
//
package medview.openehr.workbench.model;

import java.util.*;

import br.com.zilics.archetypes.models.rm.common.resource.*;
import br.com.zilics.archetypes.models.am.archetype.*;
import br.com.zilics.archetypes.models.am.archetype.ontology.*;

public class ArchetypeUtilities {

    public static String DEFAULT_LANGUAGE = "en";
    public static final String CODE_KEY = "code";
    public static final String TEXT_KEY = "text";
    public static final String DESCRIPTION_KEY = "description";
    public static final String ADL_EXTENSION = "adl";
    public static final String OET_EXTENSION = "oet";

    public static String getOriginalLanguage(Archetype a) {
        return a.getOriginalLanguage().getCodeString();
    }

    public static List<String> getAvailableLanguages(Archetype a) {

        ArrayList<String> langs = new ArrayList();
        langs.add(getOriginalLanguage(a));

        Set<TranslationDetails> tds = a.getTranslations();

        if (tds != null) {
            Iterator<TranslationDetails> it = tds.iterator();

            while (it.hasNext()) {
                TranslationDetails td = it.next();

                langs.add(td.getLanguage().getCodeString());
            }
        }
        return langs;
    }

    public static Set<String> getTranslations(Archetype a) {
        Set<TranslationDetails> tds = a.getTranslations();

        Iterator<TranslationDetails> it = tds.iterator();

        TreeSet<String> langs = new TreeSet();
        while (it.hasNext()) {
            TranslationDetails td = it.next();

            langs.add(td.getLanguage().getCodeString());
        }

        return langs;
    }

    public static String getTextValue(Archetype a, String code) {
        return getTextValue(a, code, DEFAULT_LANGUAGE);
    }

    public static String getTextValue(Archetype a, String code, String lang) {
        ArchetypeOntology ontology = a.getOntology();

        return getTextValue(ontology, code, lang);
    }

    public static String getConstraintTextValue(Archetype a, String code) {
        return getConstraintTextValue(a, code, DEFAULT_LANGUAGE);
    }

    public static String getConstraintTextValue(Archetype a, String code, String lang) {
        ArchetypeOntology ontology = a.getOntology();

        return getConstraintTextValue(ontology, code, lang);
    }
    public static String getDescriptionValue(Archetype a, String code) {
        return getDescriptionValue(a, code, DEFAULT_LANGUAGE);

    }

    public static String getDescriptionValue(Archetype a, String code, String lang) {
        ArchetypeOntology ontology = a.getOntology();

        return getDescriptionValue(ontology, code, lang);
    }

     public static String getConstraintDescriptionValue(Archetype a, String code) {
        return getConstraintDescriptionValue(a, code, DEFAULT_LANGUAGE);

    }

    public static String getConstraintDescriptionValue(Archetype a, String code, String lang) {
        ArchetypeOntology ontology = a.getOntology();

        return getConstraintDescriptionValue(ontology, code, lang);
    }
    public static String getConceptTextValue(Archetype a) {

        ArchetypeOntology ontology = a.getOntology();
        String conceptCode = a.getConcept();

        return getTextValue(ontology, conceptCode, DEFAULT_LANGUAGE);
    }

    public static String getTextValue(ArchetypeOntology o, String code, String lang) {
        String result = null;
        CodeDefinitionSet cs = (CodeDefinitionSet) o.getTermDefinitions().get(lang);
        if (cs != null) {
            ArchetypeTerm at = (ArchetypeTerm) cs.getItems().get(code);
            if (at != null) {
                result = at.getItems().get(TEXT_KEY);
            }
        }
        return result;
    }

    public static String getConstraintTextValue(ArchetypeOntology o, String code, String lang) {
        String result = null;
        CodeDefinitionSet cs = (CodeDefinitionSet) o.getConstraintDefinitions().get(lang);
        if (cs != null) {
            ArchetypeTerm at = (ArchetypeTerm) cs.getItems().get(code);
            if (at != null) {
                result = at.getItems().get(TEXT_KEY);
            }
        }
        return result;
    }

    public static String getDescriptionValue(ArchetypeOntology o, String code, String lang) {
        String result = null;
        CodeDefinitionSet cs = (CodeDefinitionSet) o.getTermDefinitions().get(lang);
        if (cs != null) {
            ArchetypeTerm at = (ArchetypeTerm) cs.getItems().get(code);
            if (at != null) {
                result = at.getItems().get(DESCRIPTION_KEY);
            }
        }
        return result;
    }

     public static String getConstraintDescriptionValue(ArchetypeOntology o, String code, String lang) {
        String result = null;
        CodeDefinitionSet cs = (CodeDefinitionSet) o.getConstraintDefinitions().get(lang);
        if (cs != null) {
            ArchetypeTerm at = (ArchetypeTerm) cs.getItems().get(code);
            if (at != null) {
                result = at.getItems().get(DESCRIPTION_KEY);
            }
        }
        return result;
    }

    public static List<String> getAtCodesList(Archetype a) {
        // System.out.println("Language: " + a.getOriginalLanguage().toString());
        String lang = a.getOriginalLanguage().getCodeString();

        return getAtCodesList(a.getOntology(), lang);
    }

    public static Set<String> getAtCodes(Archetype a) {
        String lang = a.getOriginalLanguage().getCodeString();

        return getAtCodes(a.getOntology(), lang);
    }

    public static List<String> getAcCodesList(Archetype a) {
        String lang = a.getOriginalLanguage().getCodeString();

        return getAcCodesList(a.getOntology(), lang);
    }

    public static Set<String> getAcCodes(Archetype a) {
        String lang = a.getOriginalLanguage().getCodeString();

        return getAcCodes(a.getOntology(), lang);
    }

    public static List<String> getAtCodesList(ArchetypeOntology ontology, String lang) {

        List<String> list = new ArrayList(getAtCodes(ontology, lang));

        Collections.sort(list);

        // System.out.println(list.toString());

        return list;
    }

    public static Set<String> getAtCodes(ArchetypeOntology ontology, String lang) {
        CodeDefinitionSet cs = (CodeDefinitionSet) ontology.getTermDefinitions().get(lang);

        Map<String, ArchetypeTerm> terms = cs.getItems();
        Set<String> keys = terms.keySet();

        // System.out.println(keys.toString());
        return keys;
    }

    public static List<String> getAcCodesList(ArchetypeOntology ontology, String lang) {
        List<String> list = new ArrayList(getAcCodes(ontology, lang));

        Collections.sort(list);

        // System.out.println(list.toString());

        return list;
    }

    public static Set<String> getAcCodes(ArchetypeOntology ontology, String lang) {
        CodeDefinitionSet cs = (CodeDefinitionSet) ontology.getConstraintDefinitions().get(lang);

        if (cs != null) {
            Map<String, ArchetypeTerm> terms = cs.getItems();
            Set<String> keys = terms.keySet();

            return keys;

        }
        return new TreeSet();

    }
}
