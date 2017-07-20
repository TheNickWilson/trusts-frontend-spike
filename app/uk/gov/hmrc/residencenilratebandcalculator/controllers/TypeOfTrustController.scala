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
import play.api.libs.json.{Reads, Writes}
import play.api.mvc.{Action, Request}
import uk.gov.hmrc.http.cache.client.CacheMap
import uk.gov.hmrc.play.http.logging.SessionId
import uk.gov.hmrc.residencenilratebandcalculator.connectors.SessionConnector
import uk.gov.hmrc.residencenilratebandcalculator.forms.CompanyOrIndividualForm
import uk.gov.hmrc.residencenilratebandcalculator.models.UserAnswers
import uk.gov.hmrc.residencenilratebandcalculator.views.html.type_of_trust
import uk.gov.hmrc.residencenilratebandcalculator.{Constants, FrontendAppConfig, Navigator}

class TypeOfTrustController @Inject()(override val appConfig: FrontendAppConfig,
                                      val messagesApi: MessagesApi,
                                      override val sessionConnector: SessionConnector,
                                      override val navigator: Navigator) extends SimpleControllerBase[String] {

  override val controllerId = Constants.typeOfTrustId

  override def form = () => CompanyOrIndividualForm()

  override def view(form: Option[Form[String]], userAnswers: UserAnswers)(implicit request: Request[_]) = {
    type_of_trust(appConfig, form)
  }

  override def onPageLoad(implicit rds: Reads[String]) = Action.async { implicit request =>
    sessionConnector.fetch().map {
      optionalCacheMap => {
        val cacheMap = optionalCacheMap.getOrElse(CacheMap(hc.sessionId.getOrElse(SessionId("")).value, Map()))
        val userAnswers = new UserAnswers(cacheMap)
        Ok(view(cacheMap.getEntry(controllerId).map(value => form().fill(value)), userAnswers))
      }
    }
  }

  override def onSubmit(implicit wts: Writes[String]) = Action.async { implicit request =>
    val boundForm = form().bindFromRequest()
    boundForm.fold(
      (formWithErrors: Form[String]) => {
        sessionConnector.fetch().map {
          optionalCacheMap => {
            val cacheMap = optionalCacheMap.getOrElse(CacheMap(hc.sessionId.getOrElse(SessionId("")).value, Map()))
            val userAnswers = new UserAnswers(cacheMap)
            BadRequest(view(Some(formWithErrors), userAnswers))
          }
        }
      },
      (value) =>
        sessionConnector.cache[String](controllerId, value).map(cacheMap =>
          Redirect(navigator.nextPage(controllerId)(new UserAnswers(cacheMap))))
    )
  }
}
