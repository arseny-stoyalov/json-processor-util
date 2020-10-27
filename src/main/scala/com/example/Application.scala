package com.example

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import com.typesafe.scalalogging.LazyLogging

import scala.annotation.tailrec

object Application extends App with LazyLogging {

  val availableArs = List(
    "url", "regex", "fileName"
  )

  @tailrec
  def parseArgs(arguments: Array[String], map: Map[String, String]): Map[String, String] = {
    arguments.take(2).toList match {
      case s"-${argument}" :: value :: _ if availableArs.contains(argument) =>
        parseArgs(arguments.drop(2), map + (argument -> value))
      case Nil => map
      case _ => throw new IllegalArgumentException("You messed up, buddy:)")
    }
  }

  val arguments = parseArgs(args, Map())

  logger.info(s"Got arguments $arguments")

  val cleaner = new JsonCleaner(arguments.get("regex").orNull)

  val result = cleaner.process(arguments.get("url").orNull)

  Files.write(Paths.get(arguments.get("fileName").orNull), result.getBytes(StandardCharsets.UTF_8))

}
