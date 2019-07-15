/*
 * Copyright 2013-2015 Sergey Ignatov, Alexander Zolotov, Florin Patan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.notzippy.intellij.go.intellij.project;

import com.notzippy.intellij.go.intellij.GoConstants;
import com.notzippy.intellij.go.goide.GoLibrariesState;
import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

@State(
  name = GoConstants.GO_LIBRARIES_SERVICE_NAME,
  storages = {
    @Storage(value = GoConstants.GO_LIBRARIES_CONFIG_FILE)
  }
)
public class GoProjectLibrariesService extends GoLibrariesService<GoLibrariesState> {
  public static GoProjectLibrariesService getInstance(@NotNull Project project) {
    return ServiceManager.getService(project, GoProjectLibrariesService.class);
  }
}
