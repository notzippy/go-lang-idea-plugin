/*
 * Copyright 2013-2016 Sergey Ignatov, Alexander Zolotov, Florin Patan
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

package com.notzippy.intellij.go.intellij.completion;

import com.notzippy.intellij.go.intellij.GoFileType;
import com.notzippy.intellij.go.intellij.project.GoExcludedPathsSettings;
import com.notzippy.intellij.go.intellij.project.GoVendoringUtil;
import com.notzippy.intellij.go.grammar.psi.GoFile;
import com.notzippy.intellij.go.grammar.psi.GoImportString;
import com.notzippy.intellij.go.grammar.psi.impl.GoPsiImplUtil;
import com.notzippy.intellij.go.intellij.runconfig.testing.GoTestFinder;
import com.notzippy.intellij.go.intellij.util.GoUtil;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GoImportPathsCompletionProvider extends CompletionProvider<CompletionParameters> {
  @Override
  protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
    GoImportString importString = PsiTreeUtil.getParentOfType(parameters.getPosition(), GoImportString.class);
    if (importString == null) return;
    String path = importString.getPath();
    if (path.startsWith("./") || path.startsWith("../")) return;

    TextRange pathRange = importString.getPathTextRange().shiftRight(importString.getTextRange().getStartOffset());
    String newPrefix = parameters.getEditor().getDocument().getText(TextRange.create(pathRange.getStartOffset(), parameters.getOffset()));
    result = result.withPrefixMatcher(result.getPrefixMatcher().cloneWithPrefix(newPrefix));

    Module module = ModuleUtilCore.findModuleForPsiElement(parameters.getOriginalFile());
    if (module != null) {
      addCompletions(result, module, parameters.getOriginalFile(), GoUtil.goPathResolveScope(module, parameters.getOriginalFile()), false);
    }
  }

  public static void addCompletions(@NotNull CompletionResultSet result,
                                    @NotNull Module module,
                                    @Nullable PsiElement context,
                                    @NotNull GlobalSearchScope scope,
                                    boolean allowMain) {
    Project project = module.getProject();
    boolean vendoringEnabled = GoVendoringUtil.isVendoringEnabled(module);
    String contextImportPath = GoCompletionUtil.getContextImportPath(context, vendoringEnabled);
    GoExcludedPathsSettings excludedSettings = GoExcludedPathsSettings.getInstance(project);
    PsiFile contextFile = context != null ? context.getContainingFile() : null;
    boolean testFileWithTestPackage = GoTestFinder.isTestFileWithTestPackage(contextFile);
    for (VirtualFile file : FileTypeIndex.getFiles(GoFileType.INSTANCE, scope)) {
      ProgressManager.checkCanceled();
      PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
      if (!(psiFile instanceof GoFile)) continue;
      
      PsiDirectory directory = psiFile.getContainingDirectory();
      if (directory == null) continue;

      GoFile goFile = (GoFile)psiFile;
      if (!GoPsiImplUtil.canBeAutoImported(goFile, allowMain, module)) continue;
      
      String importPath = goFile.getImportPath(vendoringEnabled);
      if (StringUtil.isNotEmpty(importPath) && !excludedSettings.isExcluded(importPath) 
          && (testFileWithTestPackage || !importPath.equals(contextImportPath))) {
        result.addElement(GoCompletionUtil.createPackageLookupElement(importPath, contextImportPath, directory, false));
      }
    }
  }
}
