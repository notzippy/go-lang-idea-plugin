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

package com.goide.inspections;

import com.goide.SdkAware;
import com.goide.quickfix.GoQuickFixTestBase;

@SdkAware
public class GoStringCannotBeNilInspectionTest extends GoQuickFixTestBase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    myFixture.enableInspections(GoStringCannotBeNilInspection.class);
  }

  public void testVarDeclaration() {
    doTest(GoStringCannotBeNilInspection.QUICK_FIX_NAME, true);
  }

  public void testAssignment() {
    doTest(GoStringCannotBeNilInspection.QUICK_FIX_NAME, true);
  }

  public void testTooManyValues() {
    doTest(GoStringCannotBeNilInspection.QUICK_FIX_NAME, true);
  }

  public void testComparison() {
    doTest(GoStringCannotBeNilInspection.QUICK_FIX_NAME, true);
  }

  public void testNilVariable() {
    myFixture.testHighlighting(getTestName(true) + ".go");
  }

  @Override
  protected String getBasePath() {
    return "inspections/string-assigned-to-nil";
  }
}
