/*
 * Copyright 2017 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.residencenilratebandcalculator.views

import uk.gov.hmrc.residencenilratebandcalculator.controllers.routes
import uk.gov.hmrc.residencenilratebandcalculator.views.html.session_expired

class SessionExpiredViewSpec extends ViewSpecBase {

  val messageKeyPrefix = "session_expired"

  "Session Expired view" must {
    "display the correct browser title" in {
      val doc = asDocument(session_expired(frontendAppConfig)(request, messages))
      assertEqualsMessage(doc, "title", s"$messageKeyPrefix.browser_title")
    }

    "display the correct title" in {
      val doc = asDocument(session_expired(frontendAppConfig)(request, messages))
      assertPageTitleEqualsMessage(doc, s"$messageKeyPrefix.title")
    }

    "display the correct guidance" in {
      val doc = asDocument(session_expired(frontendAppConfig)(request, messages))
      assertContainsMessages(doc, s"$messageKeyPrefix.guidance")
    }

    "not display the HMRC logo" in {
      val doc = asDocument(session_expired(frontendAppConfig)(request, messages))
      assertNotRenderedByCssSelector(doc, ".organisation-logo")
    }
  }
}
