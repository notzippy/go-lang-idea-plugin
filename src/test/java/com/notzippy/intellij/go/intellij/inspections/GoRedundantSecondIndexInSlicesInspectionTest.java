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

package com.notzippy.intellij.go.intellij.inspections;

import com.notzippy.intellij.go.SdkAware;
import com.notzippy.intellij.go.intellij.quickfix.GoQuickFixTestBase;
import org.jetbrains.annotations.NotNull;

@SdkAware
public class GoRedundantSecondIndexInSlicesInspectionTest extends GoQuickFixTestBase {
  @Override
  public void setUp() throws Exception {
    super.setUp();
    myFixture.enableInspections(GoRedundantSecondIndexInSlicesInspection.class);
  }

  public void testSlice() {
    doTest(GoRedundantSecondIndexInSlicesInspection.DELETE_REDUNDANT_INDEX_QUICK_FIX_NAME, true);
  }

  public void testSliceWithLenAnotherArray() {
    doTestNoFix(GoRedundantSecondIndexInSlicesInspection.DELETE_REDUNDANT_INDEX_QUICK_FIX_NAME, true);
  }

  public void testSliceWithOnlySecondIndex() {
    doTest(GoRedundantSecondIndexInSlicesInspection.DELETE_REDUNDANT_INDEX_QUICK_FIX_NAME, true);
  }

  public void testSliceWithStructIndex() {
    doTestNoFix(GoRedundantSecondIndexInSlicesInspection.DELETE_REDUNDANT_INDEX_QUICK_FIX_NAME, true);
  }

  public void testSliceWithThreeIndexes() {
    doTestNoFix(GoRedundantSecondIndexInSlicesInspection.DELETE_REDUNDANT_INDEX_QUICK_FIX_NAME, true);
  }

  public void testSliceWithRedeclaredLen() {
    doTestNoFix(GoRedundantSecondIndexInSlicesInspection.DELETE_REDUNDANT_INDEX_QUICK_FIX_NAME, true);
  }

  @NotNull
  @Override
  protected String getBasePath() {
    return "inspections/go-simplify";
  }
}
