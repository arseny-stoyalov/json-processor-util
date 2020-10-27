package com.example

import io.circe.{Json, ParsingFailure}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers
import io.circe.jawn._
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class JsonCleanerTest extends AnyFunSuite with Matchers {

  val regexToTest: String = "abc.*"

  val cleaner: JsonCleaner = new JsonCleaner(regexToTest)

  val testJsonCase: String =
    """{
      |
      |    "key1": "string1",
      |    "key2": {
      |        "innerKey1": "string1",
      |        "innerKey2": "string2",
      |        "abcdef": {
      |            "number": 123,
      |            "ab": "string1"
      |        }
      |    },
      |    "abc12": {
      |        "abc": "string1"
      |    },
      |    "key4": {
      |        "key5": {
      |            "abcrf": 1,
      |            "abd": 2,
      |            "key6": "string1"
      |        }
      |    },
      |    "abcght": "string2"
      |}
      |""".stripMargin

  val expected: String =
    """{
      |
      |    "key1": "string1",
      |    "key2": {
      |        "innerKey1": "string1",
      |        "innerKey2": "string2"
      |    },
      |    "key4": {
      |        "key5": {
      |            "abd": 2,
      |            "key6": "string1"
      |        }
      |    }
      |}
      | """.stripMargin

  test("testRecursiveCleanUp") {
    val jsonEither: Either[ParsingFailure, Json] = parse(testJsonCase)
    jsonEither shouldBe a[Right[ParsingFailure, Json]]

    val result = cleaner.recursiveCleanUp(jsonEither.getOrElse(Json.Null))
    result.toString().replaceAll("\\s+", "")
      .shouldEqual(expected.replaceAll("\\s+", ""))
  }



}
