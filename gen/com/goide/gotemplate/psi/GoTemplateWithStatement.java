// This is a generated file. Not intended for manual editing.
package com.goide.gotemplate.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GoTemplateWithStatement extends GoTemplateStatement {

  @Nullable
  GoTemplateEndStatement getEndStatement();

  @Nullable
  GoTemplatePipeline getPipeline();

  @Nullable
  GoTemplateStatementList getStatementList();

}