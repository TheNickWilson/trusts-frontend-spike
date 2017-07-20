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

import play.api.data.Form
import uk.gov.hmrc.residencenilratebandcalculator.models.CompanySettlor
import uk.gov.hmrc.residencenilratebandcalculator.views.html.company_settlor

class CompanySettlorViewSpec extends ViewSpecBase {

  val messageKeyPrefix = "company_settlor"

  def createView(form: Option[Form[CompanySettlor]] = None) = company_settlor(frontendAppConfig, form)(request, messages)

  "Company settlor view" must {
    behave like trustsPage[CompanySettlor](createView, messageKeyPrefix)
  }
}
