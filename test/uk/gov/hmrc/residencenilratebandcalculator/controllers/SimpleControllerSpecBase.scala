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

import akka.util.ByteString
import play.api.http.Status
import play.api.i18n._
import play.api.libs.json._
import play.api.libs.streams.Accumulator
import play.api.mvc.{AnyContentAsFormUrlEncoded, AnyContentAsJson, Result}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.http.cache.client.CacheMap
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}
import uk.gov.hmrc.residencenilratebandcalculator.mocks.HttpResponseMocks
import uk.gov.hmrc.residencenilratebandcalculator.models.UserAnswers
import uk.gov.hmrc.residencenilratebandcalculator.{Constants, FrontendAppConfig, Navigator}
import uk.gov.hmrc.residencenilratebandcalculator.models._

import scala.concurrent.Future
import scala.reflect.ClassTag

trait SimpleControllerSpecBase extends UnitSpec with WithFakeApplication with HttpResponseMocks with MockSessionConnector {

  val fakeRequest = FakeRequest("", "")

  val injector = fakeApplication.injector

  val navigator = injector.instanceOf[Navigator]

  def frontendAppConfig = injector.instanceOf[FrontendAppConfig]

  def messagesApi = injector.instanceOf[MessagesApi]

  def messages = messagesApi.preferred(fakeRequest)

  def questionController[A: ClassTag](createController: () => ControllerBase[A],
                                      createView: (Option[A]) => HtmlFormat.Appendable,
                                      cacheKey: String,
                                      validData: A,
                                      invalidData: JsValue,
                                      fakeValidRequest: FakeRequest[AnyContentAsJson],
                                      fakeInvalidRequest: FakeRequest[AnyContentAsJson],
                                      valuesToCacheBeforeSubmission: Map[String, A] = Map[String, A](),
                                      valuesToCacheBeforeLoad: Map[String, Any] = Map[String, Any]())
                                     (rds: Reads[A], wts: Writes[A]) = {

    "return 200 for a GET" in {
      for (v <- valuesToCacheBeforeLoad) setCacheValue(v._1, v._2)
      val result = createController().onPageLoad(rds)(fakeRequest)
      status(result) shouldBe Status.OK
    }

    "return the View for a GET" in {
      for (v <- valuesToCacheBeforeLoad) setCacheValue(v._1, v._2)
      val result = createController().onPageLoad(rds)(fakeRequest)
      contentAsString(result) shouldBe createView(None).toString
    }

    "return a redirect on submit with valid data" in {
      for (v <- valuesToCacheBeforeSubmission) setCacheValue(v._1, v._2)
      val result = createController().onSubmit(wts)(fakeValidRequest)
      status(result) shouldBe Status.SEE_OTHER
    }

    "store valid submitted data" in {
      for (v <- valuesToCacheBeforeSubmission) setCacheValue(v._1, v._2)
      await (createController().onSubmit(wts)(fakeValidRequest))
      verifyValueIsCached(cacheKey, validData)
    }

    "return bad request on submit with invalid data" in {
      val result = createController().onSubmit(wts)(fakeInvalidRequest)
      status(result) shouldBe Status.BAD_REQUEST
    }

    "not store invalid submitted data" in {
      createController().onSubmit(wts)(fakeInvalidRequest)
      verifyValueIsNotCached()
    }

    "get a previously stored value from keystore" in {
      for (v <- valuesToCacheBeforeLoad) setCacheValue(v._1, v._2)
      setCacheValue(cacheKey, validData)
      val result = createController().onPageLoad(rds)(fakeRequest)
      contentAsString(result) shouldBe createView(Some(validData)).toString
    }
  }

  def singleQuestionController[A: ClassTag](createController: () => ControllerBase[A],
                                            createView: (Option[A]) => HtmlFormat.Appendable,
                                            cacheKey: String,
                                            validData: A,
                                            invalidData: JsValue,
                                            valuesToCacheBeforeSubmission: Map[String, A] = Map[String, A](),
                                            valuesToCacheBeforeLoad: Map[String, Any] = Map[String, Any]())
                                           (rds: Reads[A], wts: Writes[A]) = {

    val validJson = Json.parse(s"""{"value" : "${validData.toString}"}""")

    val fakeValidRequest = fakeRequest.withJsonBody(validJson)

    val fakeInvalidRequest = fakeRequest.withJsonBody(invalidData)

    questionController(createController, createView, cacheKey, validData, invalidData, fakeValidRequest,
      fakeInvalidRequest, valuesToCacheBeforeSubmission, valuesToCacheBeforeLoad)(rds, wts)
  }

  def multiQuestionController[A: ClassTag](createController: () => ControllerBase[A],
                                            createView: (Option[A]) => HtmlFormat.Appendable,
                                            cacheKey: String,
                                            validData: A,
                                            invalidData: JsValue,
                                            valuesToCacheBeforeSubmission: Map[String, A] = Map[String, A](),
                                            valuesToCacheBeforeLoad: Map[String, Any] = Map[String, Any]())
                                           (rds: Reads[A], wts: Writes[A]) = {

    val fakeValidRequest = fakeRequest.withJsonBody(Json.toJson(validData)(wts))

    val fakeInvalidRequest = fakeRequest.withJsonBody(invalidData)

    questionController(createController, createView, cacheKey, validData, invalidData, fakeValidRequest,
      fakeInvalidRequest, valuesToCacheBeforeSubmission, valuesToCacheBeforeLoad)(rds, wts)
  }
}
