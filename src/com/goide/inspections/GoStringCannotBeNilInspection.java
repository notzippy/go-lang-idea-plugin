package com.goide.inspections;

import com.goide.GoConstants;
import com.goide.GoTypes;
import com.goide.psi.*;
import com.goide.psi.impl.GoElementFactory;
import com.goide.psi.impl.GoPsiImplUtil;
import com.goide.psi.impl.GoTypeUtil;
import com.intellij.codeInspection.*;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.IntStream;

public class GoStringCannotBeNilInspection extends GoInspectionBase {
  public static final String QUICK_FIX_NAME = "Change to default value";
  private static final String DEFAULT_STRING = "\"\"";
  private static final String PROBLEM_DESCRIPTION = "String cannot be nil";

  @NotNull
  @Override
  protected GoVisitor buildGoVisitor(@NotNull ProblemsHolder holder, @NotNull LocalInspectionToolSession session) {
    return new GoVisitor() {

      @Override
      public void visitVarSpec(@NotNull GoVarSpec o) {
        super.visitVarSpec(o);

        if (o.getVarDefinitionList() == null) return;
        o.getVarDefinitionList().forEach(var -> check(var, var != null ? var.getValue() : null));
      }

      @Override
      public void visitAssignmentStatement(@NotNull GoAssignmentStatement o) {
        super.visitAssignmentStatement(o);

        if (o.getLeftHandExprList() == null) return;
        List<GoExpression> rightSide = o.getExpressionList();
        List<GoExpression> leftSide = o.getLeftHandExprList().getExpressionList();
        if (leftSide == null || rightSide == null) return;

        IntStream.range(0, Math.min(leftSide.size(), rightSide.size()))
          .forEach(i -> check(leftSide.get(i), rightSide.get(i)));
      }

      @Override
      public void visitBinaryExpr(@NotNull GoBinaryExpr o) {
        super.visitBinaryExpr(o);
        if (o.getOperator() == null || o.getOperator().getNode() == null) return;
        IElementType type = o.getOperator().getNode().getElementType();
        if (type == GoTypes.EQ || type == GoTypes.NOT_EQ) {
          check(o.getLeft(), o.getRight());
          check(o.getRight(), o.getLeft());
        }
      }

      protected void check(GoTypeOwner var, GoExpression value) {

        if (var == null || value == null) return;
        GoType varType = var.getGoType(null);
        if (!GoTypeUtil.isString(varType)) return;

        if (value instanceof GoReferenceExpression && value.textMatches(GoConstants.NIL)
            && GoPsiImplUtil.builtin(((GoReferenceExpression)value).resolve())) {

          holder.registerProblem(value, PROBLEM_DESCRIPTION, ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                                 new GoChangeStringToDefaultValueQuickFix());
        }
      }
    };
  }

  private static class GoChangeStringToDefaultValueQuickFix extends LocalQuickFixBase {
    public GoChangeStringToDefaultValueQuickFix() {
      super(QUICK_FIX_NAME);
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
      PsiElement element = descriptor.getPsiElement();
      if (element == null || !element.isValid()) return;
      if (element instanceof GoExpression) {
        element.replace(GoElementFactory.createExpression(project, DEFAULT_STRING));
      }
    }
  }
}
