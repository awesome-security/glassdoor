package io.glassdoor.plugin

/**
  * Created by Florian Schrofner on 4/5/16.
  */
case class PluginInstance(name:String, kind:String, dependencies:Array[String], commands:Array[String], plugin:Plugin)
