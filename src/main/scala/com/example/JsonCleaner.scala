package com.example

import io.circe._
import io.circe.jawn._
import io.circe.syntax._

import scala.io.Source

class JsonCleaner(regex: String) {

  def process(url: String): String = {
    val src = Source.fromURL(url)
    val rawJson = src.mkString
    src.close()

    val json = parse(rawJson)
      .getOrElse(throw new Exception("Could not parse json"))

    recursiveCleanUp(json).toString()

  }

  def recursiveCleanUp(json: Json): Json = {
    if (json.isObject) {
      val res = json.mapObject(_.filter(!_._1.matches(regex)))
      res.asObject.get.mapValues(recursiveCleanUp).asJson
    }
    else json
  }

}
