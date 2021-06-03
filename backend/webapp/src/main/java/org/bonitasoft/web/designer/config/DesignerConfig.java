/**
 * Copyright (C) 2015 Bonitasoft S.A.
 * Bonitasoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.designer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.bonitasoft.web.designer.ArtifactBuilder;
import org.bonitasoft.web.designer.ArtifactBuilderFactory;
import org.bonitasoft.web.designer.JsonHandlerFactory;
import org.bonitasoft.web.designer.UiDesignerCore;
import org.bonitasoft.web.designer.UiDesignerCoreFactory;
import org.bonitasoft.web.designer.controller.asset.AssetService;
import org.bonitasoft.web.designer.livebuild.Watcher;
import org.bonitasoft.web.designer.model.JacksonJsonHandler;
import org.bonitasoft.web.designer.model.JsonHandler;
import org.bonitasoft.web.designer.model.fragment.Fragment;
import org.bonitasoft.web.designer.model.page.Page;
import org.bonitasoft.web.designer.model.widget.Widget;
import org.bonitasoft.web.designer.repository.AssetRepository;
import org.bonitasoft.web.designer.repository.FragmentRepository;
import org.bonitasoft.web.designer.repository.PageRepository;
import org.bonitasoft.web.designer.repository.Repository;
import org.bonitasoft.web.designer.repository.WidgetRepository;
import org.bonitasoft.web.designer.service.BondsTypesFixer;
import org.bonitasoft.web.designer.service.FragmentService;
import org.bonitasoft.web.designer.service.PageService;
import org.bonitasoft.web.designer.service.WidgetService;
import org.bonitasoft.web.designer.workspace.ResourcesCopier;
import org.bonitasoft.web.designer.workspace.Workspace;
import org.fedorahosted.tennera.jgettext.PoParser;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author Guillaume EHRET
 */
@Configuration
@EnableScheduling
@RequiredArgsConstructor
@ConfigurationPropertiesScan(basePackageClasses = UiDesignerProperties.class)
public class DesignerConfig {

    private final UiDesignerProperties designerProperties;

    private JsonHandler jsonHandler;

    private UiDesignerCoreFactory coreFactory;

    @PostConstruct
    public void initialize()  throws Exception{
        jsonHandler = new JsonHandlerFactory().create();
        coreFactory = new UiDesignerCoreFactory(designerProperties, jsonHandler);
    }

    @Bean
    public UiDesignerCore uidCore(
            WidgetRepository widgetRepository,
            AssetRepository<Widget> widgetAssetRepository,
            FragmentRepository fragmentRepository,
            PageRepository pageRepository,
            AssetRepository<Page> pageAssetRepository
    ) {
        return coreFactory.create(widgetRepository, widgetAssetRepository, fragmentRepository, pageRepository, pageAssetRepository);
    }

    @Bean
    public Watcher watcher(UiDesignerCore uiDesignerCore) {
        return uiDesignerCore.getWatcher();
    }

    @Bean
    public ResourcesCopier resourcesCopier() {
        return new ResourcesCopier();
    }

    /**
     * We use our own jackson object Mapper
     */
    @Bean
    public JsonHandler jsonHandler() {
        return new JsonHandlerFactory().create();
    }

    @Bean
    public ObjectMapper objectMapper(JsonHandler jsonHandler) {
        return ((JacksonJsonHandler) jsonHandler).getObjectMapper();
    }

    @Bean
    public WidgetRepository widgetRepository() {
        return coreFactory.createWidgetRepository();
    }

    @Bean
    public PageService pageService(UiDesignerCore uiDesignerCore) {
        return uiDesignerCore.getPageService();
    }

    @Bean
    public FragmentService fragmentService(UiDesignerCore uiDesignerCore) {
        return uiDesignerCore.getFragmentService();
    }

    @Bean
    public WidgetService widgetService(UiDesignerCore uiDesignerCore) {
        return uiDesignerCore.getWidgetService();
    }

    @Bean
    public AssetRepository<Widget> widgetAssetRepository(WidgetRepository widgetRepository) {
        return coreFactory.createWidgetAssetRepository(widgetRepository);
    }

    @Bean
    public FragmentRepository fragmentRepository() {
        return coreFactory.createFragmentRepository();
    }

    @Bean
    public PageRepository pageRepository() {
        return coreFactory.createPageRepository();
    }

    @Bean
    public AssetRepository<Page> pageAssetRepository(PageRepository pageRepository) {
        return coreFactory.createPageAssetRepository(pageRepository);
    }

    @Bean
    public ArtifactBuilder artifactBuilder(UiDesignerCore uidCore) {
        return new ArtifactBuilderFactory(designerProperties, jsonHandler, uidCore).create();
    }

    @Bean
    public Workspace workspace(ArtifactBuilder artifactBuilder) {
        return artifactBuilder.getWorkspace();
    }

    /**
     * Used by internationalisation to work on PO files
     */
    @Bean
    public PoParser poParser() {
        return new PoParser();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public AssetService<Page> pageAssetService(UiDesignerCore uidCore) {
        return uidCore.getPageAssetService();
    }

    @Bean
    public AssetService<Widget> widgetAssetService(UiDesignerCore uidCore) {
        return uidCore.getWidgetAssetService();
    }

    @Bean
    public BondsTypesFixer<Page> pageBondsTypesFixer(PageRepository pageRepository) {
        return new BondsTypesFixer<>(pageRepository);
    }

    @Bean
    public List<Repository> fragmentsUsedByRepositories(PageRepository pageRepository, FragmentRepository fragmentRepository) {
        return List.of(pageRepository, fragmentRepository);
    }

    @Bean
    public BondsTypesFixer<Fragment> fragmentBondsTypesFixer(FragmentRepository fragmentRepository) {
        return new BondsTypesFixer<>(fragmentRepository);
    }

}
