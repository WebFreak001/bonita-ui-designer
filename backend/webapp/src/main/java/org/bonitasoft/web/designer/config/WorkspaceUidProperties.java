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

import java.nio.file.Path;
import javax.inject.Named;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
@Named
public class WorkspaceUidProperties {

    public static final String FRAGMENTS = "fragments";
    private static final String PAGES_DEFAULT_DIRECTORY = "pages";

    @Value("${designer.workspace-uid.path}")
    private Path path;

    public Path getTmpFragmentsRepositoryPath() {
        return path.resolve(FRAGMENTS);
    }

    public Path getTmpPagesRepositoryPath() {
        return path.resolve(PAGES_DEFAULT_DIRECTORY);
    }
}
