/*
 * Copyright 2019-2020 Not zippy
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

// This is a generated file. Not intended for manual editing.
package com.notzippy.intellij.go.grammar.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.notzippy.intellij.go.grammar.psi.GoPsiTreeUtil;
import static com.notzippy.intellij.go.parser.GoTypes.*;
import com.notzippy.intellij.go.stubs.GoImportSpecStub;
import com.notzippy.intellij.go.grammar.psi.*;
import com.intellij.psi.stubs.IStubElementType;

public class GoImportSpecImpl extends GoNamedElementImpl<GoImportSpecStub> implements GoImportSpec {

  public GoImportSpecImpl(@NotNull GoImportSpecStub stub, @NotNull IStubElementType type) {
    super(stub, type);
  }

  public GoImportSpecImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GoVisitor visitor) {
    visitor.visitImportSpec(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GoVisitor) accept((GoVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public GoImportString getImportString() {
    return notNullChild(GoPsiTreeUtil.getChildOfType(this, GoImportString.class));
  }

  @Override
  @Nullable
  public PsiElement getDot() {
    return findChildByType(DOT);
  }

  @Override
  @Nullable
  public PsiElement getIdentifier() {
    return findChildByType(IDENTIFIER);
  }

  @Override
  public String getAlias() {
    return GoPsiImplUtil.getAlias(this);
  }

  @Override
  public String getLocalPackageName() {
    return GoPsiImplUtil.getLocalPackageName(this);
  }

  @Override
  public boolean shouldGoDeeper() {
    return GoPsiImplUtil.shouldGoDeeper(this);
  }

  @Override
  public boolean isForSideEffects() {
    return GoPsiImplUtil.isForSideEffects(this);
  }

  @Override
  public boolean isDot() {
    return GoPsiImplUtil.isDot(this);
  }

  @Override
  @NotNull
  public String getPath() {
    return GoPsiImplUtil.getPath(this);
  }

  @Override
  public String getName() {
    return GoPsiImplUtil.getName(this);
  }

  @Override
  public boolean isCImport() {
    return GoPsiImplUtil.isCImport(this);
  }

}
