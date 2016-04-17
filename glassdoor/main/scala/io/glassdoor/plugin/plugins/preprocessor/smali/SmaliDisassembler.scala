package io.glassdoor.plugin.plugins.preprocessor.smali

import java.io.File

import io.glassdoor.application.{ContextConstant, Constant, Context}
import io.glassdoor.plugin.Plugin
import org.jf.baksmali.{baksmaliOptions, baksmali}
import org.jf.dexlib2.{DexFileFactory, Opcodes}
import org.jf.dexlib2.iface.{ClassDef, DexFile}

/**
  * Created by Florian Schrofner on 3/16/16.
  */
class SmaliDisassembler extends Plugin{
  var mContext:Option[Context] = None

  override def apply(context: Context, parameters: Array[String]): Unit = {
    //baksmali.disassembleDexFile(context.intermediateAssembly(Constant.INTERMEDIATE_ASSEMBLY_DEX))
    //val folder = new File(context.intermediateAssembly(Constant.INTERMEDIATE_ASSEMBLY_DEX))

    val workingDir = context.getResolvedValue(ContextConstant.FullKey.CONFIG_WORKING_DIRECTORY)

    if(workingDir.isDefined){
      val outputDirectory = workingDir.get + "/" + ContextConstant.Key.SMALI

      val options = new baksmaliOptions
      options.jobs = 1
      options.outputDirectory = outputDirectory

      val destination = context.getResolvedValue(ContextConstant.FullKey.CONFIG_WORKING_DIRECTORY)

      //TODO: use destination

      val dexFilePath = context.getResolvedValue(ContextConstant.FullKey.INTERMEDIATE_ASSEMBLY_DEX)

      if(dexFilePath.isDefined){
        val dexFileFile = new File(dexFilePath.get + "/classes.dex")
        val dexFile = DexFileFactory.loadDexFile(dexFileFile, options.dexEntry, options.apiLevel, options.experimental);

        try {
          baksmali.disassembleDexFile(dexFile, options)
          println("disassembling dex to: " + outputDirectory)
          context.setResolvedValue(ContextConstant.FullKey.INTERMEDIATE_ASSEMBLY_SMALI, outputDirectory)
          mContext = Some(context)
        } catch {
          case e:IllegalArgumentException =>
            mContext = None
        }
      } else {
        println("dex not defined!")
      }
    }


  }

  override def result:Option[Context] = {
    mContext
  }

  override def help(parameters: Array[String]): Unit = ???
}
