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

package com.notzippy.intellij.go.intellij.inspections.unresolved;

import com.notzippy.intellij.go.intellij.inspections.GoInspectionBase;
import com.notzippy.intellij.go.grammar.psi.GoConstDefinition;
import com.notzippy.intellij.go.grammar.psi.GoReferenceExpression;
import com.notzippy.intellij.go.grammar.psi.GoVisitor;
import com.intellij.codeInsight.highlighting.ReadWriteAccessDetector;
import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import static com.intellij.codeInspection.ProblemHighlightType.GENERIC_ERROR_OR_WARNING;

public class GoAssignmentToConstantInspection extends GoInspectionBase {
  @NotNull
  @Override
  protected GoVisitor buildGoVisitor(@NotNull ProblemsHolder holder, @NotNull LocalInspectionToolSession session) {
    return new GoVisitor() {
      @Override
      public void visitReferenceExpression(@NotNull GoReferenceExpression o) {
        super.visitReferenceExpression(o);
        if (o.getReadWriteAccess() != ReadWriteAccessDetector.Access.Read) {
          PsiElement resolve = o.resolve();
          if (resolve instanceof GoConstDefinition) {
            holder.registerProblem(o, "Cannot assign to constant", GENERIC_ERROR_OR_WARNING);
          }
        }
      }
    };
  }
}