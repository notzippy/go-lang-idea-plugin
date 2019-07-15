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

package com.goide.quickfix;

import com.goide.SdkAware;
import com.goide.inspections.unresolved.GoUnresolvedReferenceInspection;
import org.jetbrains.annotations.NotNull;

@SdkAware
public class GoAddStructFieldQuickFixTest extends GoQuickFixTestBase {

  private static final String ADD_STRUCT_FIELD = "Add missing field";

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    myFixture.enableInspections(GoUnresolvedReferenceInspection.class);
  }

  @NotNull
  @Override
  protected String getBasePath() {
    return "quickfixes/add-struct-field";
  }

  public void testSimple()            { doTest(ADD_STRUCT_FIELD); }
  public void testWithoutElements()   { doTest(ADD_STRUCT_FIELD); }
  public void testComplexType()       { doTest(ADD_STRUCT_FIELD); }
  public void testWithoutAssignment() { doTest(ADD_STRUCT_FIELD); }
  public void testBlank()             { doTestNoFix(ADD_STRUCT_FIELD, true); }
}
