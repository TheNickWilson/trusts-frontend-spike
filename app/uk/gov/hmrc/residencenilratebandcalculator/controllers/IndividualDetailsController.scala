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

package uk.gov.hmrc.residencenilratebandcalculator.controllers

import javax.inject.Inject

import play.api.data.Form
import play.api.i18n.MessagesApi
import play.api.mvc.Request
import uk.gov.hmrc.residencenilratebandcalculator.{Constants, FrontendAppConfig, Navigator}
import uk.gov.hmrc.residencenilratebandcalculator.connectors.SessionConnector
import uk.gov.hmrc.residencenilratebandcalculator.forms.IndividualDetailsForm
import uk.gov.hmrc.residencenilratebandcalculator.models.{CompanyDetails, IndividualDetails, UserAnswers}
import uk.gov.hmrc.residencenilratebandcalculator.views.html.individual_details

class IndividualDetailsController @Inject()(override val appConfig: FrontendAppConfig,
                                            val messagesApi: MessagesApi,
                                            override val sessionConnector: SessionConnector,
                                            override val navigator: Navigator) extends SimpleControllerBase[IndividualDetails]{

  override val controllerId = Constants.individualDetailsId

  override def form = () => IndividualDetailsForm()

  override def view(form: Option[Form[IndividualDetails]], userAnswers: UserAnswers)(implicit request: Request[_]) = {
    individual_details(appConfig, form)
  }
}
