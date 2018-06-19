package org.hesperides.domain.modules.entities;

import lombok.Value;
import org.hesperides.domain.technos.entities.Techno;
import org.hesperides.domain.templatecontainer.entities.Template;
import org.hesperides.domain.templatecontainer.entities.TemplateContainer;

import java.util.List;

@Value
public class Module extends TemplateContainer {

    List<Techno> technos;
    Long versionId;

    public Module(TemplateContainer.Key key, List<Template> templates, List<Techno> technos, Long versionId) {
        //TODO Est-ce qu'il faut générer le model ici ?
        super(key, templates, null);
        this.technos = technos;
        this.versionId = versionId;
    }

    public static class Key extends TemplateContainer.Key {

        private static final String URI_PREFIX = "/modules";
        private static final String NAMESPACE_PREFIX = "modules";
        private static final String TOSTRING_PREFIX = "module";

        public Key(String name, String version, VersionType versionType) {
            super(name, version, versionType);
        }

        @Override
        protected String getUriPrefix() {
            return URI_PREFIX;
        }

        @Override
        protected String getNamespacePrefix() {
            return NAMESPACE_PREFIX;
        }

        public String toString() {
            return TOSTRING_PREFIX + super.toString();
        }
    }
}
