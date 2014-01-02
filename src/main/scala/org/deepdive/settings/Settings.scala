package org.deepdive.settings

import com.typesafe.config._
import scala.collection.JavaConversions._
import scala.util.Try


object Settings {
  def loadFromConfig(config: Config) = SettingsParser.loadFromConfig(config)
  def loadDefault() = loadFromConfig(ConfigFactory.load)
}

trait SettingsImpl {

  def connection : Connection
  def schemaSettings : SchemaSettings
  def extractionSettings : ExtractionSettings
  def inferenceSettings : InferenceSettings

  def findExtractor(name: String) : Option[Extractor] = extractionSettings.extractors.find(_.name == name)
  
  def findExtractorDependencies(extractor: Extractor) : Set[String] = {
    extractor.dependencies.flatMap(findExtractor).flatMap(findExtractorDependencies)
  }

}

case class Settings(connection: Connection, 
  schemaSettings : SchemaSettings,
  extractionSettings: ExtractionSettings,
  inferenceSettings: InferenceSettings, 
  calibrationSettings: CalibrationSettings, 
  samplerSettings: SamplerSettings) extends SettingsImpl

