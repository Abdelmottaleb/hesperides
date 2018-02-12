package org.hesperides.domain.modules.exceptions;

import org.hesperides.domain.modules.Template;

public class DuplicateTemplateCreationException extends RuntimeException {
    public DuplicateTemplateCreationException(Template template) {
        super("le template " + template.getName() + " existe déjà.");
    }
}
