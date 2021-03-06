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

package com.notzippy.intellij.go.intellij.runconfig.file;

import com.notzippy.intellij.go.intellij.runconfig.GoRunConfigurationProducerBase;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class GoRunFileConfigurationProducer extends GoRunConfigurationProducerBase<GoRunFileConfiguration> implements Cloneable {
  public GoRunFileConfigurationProducer() {
    super(GoRunFileConfigurationType.getInstance());
  }

  @Override
  @NotNull
  protected String getConfigurationName(@NotNull PsiFile file) {
    return "go run " + file.getName();
  }
}
