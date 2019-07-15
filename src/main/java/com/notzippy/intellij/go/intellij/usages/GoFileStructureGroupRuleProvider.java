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

package com.notzippy.intellij.go.intellij.usages;

import com.notzippy.intellij.go.grammar.psi.GoFunctionOrMethodDeclaration;
import com.notzippy.intellij.go.grammar.psi.GoNamedElement;
import com.notzippy.intellij.go.grammar.psi.GoTypeSpec;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.usages.PsiElementUsageGroupBase;
import com.intellij.usages.Usage;
import com.intellij.usages.UsageGroup;
import com.intellij.usages.UsageTarget;
import com.intellij.usages.impl.FileStructureGroupRuleProvider;
import com.intellij.usages.rules.PsiElementUsage;
import com.intellij.usages.rules.SingleParentUsageGroupingRule;
import com.intellij.usages.rules.UsageGroupingRule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GoFileStructureGroupRuleProvider implements FileStructureGroupRuleProvider {
  public static final SingleParentUsageGroupingRule USAGE_GROUPING_RULE = new GoClassGroupingRule();
  public UsageGroupingRule getUsageGroupingRule(Project project) {
    return new GoClassGroupingRule();
  }
  private static class GoClassGroupingRule extends SingleParentUsageGroupingRule {
    @Nullable
    @Override
    protected UsageGroup getParentGroupFor(@NotNull Usage usage, @NotNull UsageTarget[] targets) {
      if (!(usage instanceof PsiElementUsage)) return null;
      final PsiElement psiElement = ((PsiElementUsage)usage).getElement();
      GoNamedElement topmostElement = PsiTreeUtil.getParentOfType(psiElement, GoTypeSpec.class, GoFunctionOrMethodDeclaration.class);
      if (topmostElement != null) {
        return new PsiElementUsageGroupBase<>(topmostElement);
      }
      return null;
    }
  }
}
