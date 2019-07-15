/*
 * Copyright 2013-2017 Sergey Ignatov, Alexander Zolotov, Florin Patan
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

package com.goide.inspections.unresolved;

import com.goide.GoConstants;
import com.goide.psi.*;
import com.goide.psi.impl.GoPsiImplUtil;
import com.goide.util.GoUtil;
import com.intellij.codeInsight.template.Template;
import com.intellij.codeInsight.template.TemplateManager;
import com.intellij.codeInsight.template.impl.ConstantNode;
import com.intellij.codeInspection.LocalQuickFixAndIntentionActionOnPsiElement;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ObjectUtils;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GoAddStructFieldFix extends LocalQuickFixAndIntentionActionOnPsiElement {
  public static final String QUICK_FIX_NAME = "Add missing field";

  protected GoAddStructFieldFix(@NotNull PsiElement element) {
    super(element);
  }

  @NotNull
  @Override
  public String getText() {
    return QUICK_FIX_NAME;
  }

  @Override
  public void invoke(@NotNull Project project,
                     @NotNull PsiFile file,
                     @Nullable Editor editor,
                     @NotNull PsiElement startElement,
                     @NotNull PsiElement endElement) {
    if (editor == null) return;
    GoReferenceExpression referenceExpression = ObjectUtils.tryCast(startElement, GoReferenceExpression.class);
    GoStructType structType = referenceExpression != null ? resolveStructType(referenceExpression) : null;
    if (structType == null) return;

    List<GoFieldDeclaration> declarations = structType.getFieldDeclarationList();
    PsiElement anchor = !declarations.isEmpty() ? ContainerUtil.getLastItem(declarations) : structType.getLbrace();
    if (anchor == null) return;

    startTemplate(project, editor, file, referenceExpression, anchor);
  }

  private static void startTemplate(@NotNull Project project,
                                    @NotNull Editor editor,
                                    @NotNull PsiFile file,
                                    GoReferenceExpression referenceExpression,
                                    PsiElement anchor) {
    Template template = TemplateManager.getInstance(project).createTemplate("", "");
    template.addTextSegment(referenceExpression.getReference().getCanonicalText() + " ");
    template.addVariable(new ConstantNode(getTypeName(referenceExpression, file)), true);
    template.addTextSegment("\n");
    editor.getCaretModel().moveToOffset(anchor.getTextRange().getEndOffset() + 1);
    template.setToReformat(true);
    TemplateManager.getInstance(project).startTemplate(editor, template);
  }


  private static String getTypeName(GoReferenceExpression referenceExpression, PsiFile file) {
    GoAssignmentStatement assignment = PsiTreeUtil.getParentOfType(referenceExpression, GoAssignmentStatement.class);
    if (assignment == null) return GoConstants.INTERFACE_TYPE;
    GoExpression expression = GoPsiImplUtil.getRightExpression(assignment, referenceExpression);
    GoType type = expression != null ? expression.getGoType(null) : null;

    if (type instanceof GoSpecType) {
      GoSpecType spec = (GoSpecType)type;
      GoFile typeFile = ObjectUtils.tryCast(spec.getContainingFile(), GoFile.class);
      if (typeFile != null && (file.isEquivalentTo(typeFile) || GoUtil.inSamePackage(typeFile, file))) {
        return spec.getIdentifier().getText();
      }
    }
    return type != null ? GoPsiImplUtil.getText(type) : GoConstants.INTERFACE_TYPE;
  }

  @Nullable
  private static GoStructType resolveStructType(@NotNull GoReferenceExpression referenceExpression) {
    GoReferenceExpression qualifier = referenceExpression.getQualifier();
    GoSpecType type = qualifier != null ? ObjectUtils.tryCast(qualifier.getGoType(null), GoSpecType.class) : null;
    return type != null ? ObjectUtils.tryCast(type.getType(), GoStructType.class) : null;
  }

  @Nls
  @NotNull
  @Override
  public String getFamilyName() {
    return QUICK_FIX_NAME;
  }
}
