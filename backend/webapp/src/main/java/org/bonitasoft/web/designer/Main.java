package org.bonitasoft.web.designer;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import org.bonitasoft.web.designer.config.UiDesignerProperties;
import org.bonitasoft.web.designer.config.WorkspaceProperties;
import org.bonitasoft.web.designer.config.WorkspaceUidProperties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import static org.springframework.util.StringUtils.hasText;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties({ UiDesignerProperties.class, WorkspaceUidProperties.class, WorkspaceProperties.class })
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class Main {

    @Inject
    private WorkspaceProperties workspaceProperties;

    @Inject
    private WorkspaceUidProperties workspaceUidProperties;

    @Inject
    private UiDesignerProperties uiDesignerProperties;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @PostConstruct
    public void initialize() throws IOException {
        log.info("PagesRepositoryPath: {}", workspaceProperties.getPages().getDir());
        log.info("TmpPagesRepositoryPath: {}", workspaceUidProperties.getTmpPagesRepositoryPath());

        log.info("FragmentsRepositoryPath: {}", workspaceProperties.getFragments().getDir());
        log.info("TmpFragmentsRepositoryPath: {}", workspaceUidProperties.getTmpFragmentsRepositoryPath());

        log.info("WidgetsRepositoryPath: {}", workspaceProperties.getWidgets().getDir());
        log.info("WidgetsWcRepositoryPath: {}", workspaceProperties.getWidgetsWc().getDir());

        log.info("WorkspacePath: {}", workspaceProperties.getPath());
        log.info("TemporaryWorkspacePath: {}", workspaceUidProperties.getPath());

        log.info("TmpI18nRepositoryPath: {}", workspaceUidProperties.getTmpI18nPath());

        if (!hasText(uiDesignerProperties.getBonita().getBdm().getUrl())) {
            log.warn("BDM API url not set or empty ! No synchronization will be performed between Studio and UID on BDM objects");
        }
    }
}
