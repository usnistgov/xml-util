package nist.xml.util

import java.io.{File, StringReader, InputStream}
import java.net.URL

import org.apache.commons.io.{FileUtils, IOUtils}

import scala.util.Failure
import scala.util.Success
import scala.util.Try

import org.w3c.dom.ls.LSResourceResolver
import org.xml.sax.ErrorHandler
import org.xml.sax.SAXParseException
import org.xml.sax.XMLReader

import javax.xml.XMLConstants
import javax.xml.parsers.SAXParserFactory
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory
import javax.xml.validation.Schema
import nu.xom.Builder
import nu.xom.Document
import nu.xom.ParsingException
import nu.xom.ValidityException

import scala.xml.SAXException

/**
  * @author Salifou Sidi M. Malick <salifou.sidi@gmail.com>
  */

object XOMDocumentBuilder {

    /**
    * Build the nu.xom.Document representing the xml and validate against the schema 
    */
  def build(xml: InputStream, xsd: InputStream, resolver: Option[LSResourceResolver] = None): Try[Document] = {
        createXMLReader(xsd, resolver) match {
          case Success(reader) =>
            try {
              // when using new Builder( reader, true ) the code fails with "Document is invalid: no grammar found."
              // It seems like it is trying to validate using internal DTD or schema
              var builder : Builder = null;
              if(reader != null){
                builder = new Builder(reader, false)
              } else {
                builder = new Builder
              }
              Success(builder.build(xml))
            } catch {
              case e: ValidityException => Failure(new Error(msg(e)))
              case e: ParsingException => Failure(new Error(msg(e)))
              case e: Exception => Failure(new Error(s"[Error] Unable to parse the XML document. ${e.getMessage}"))
            }
          case Failure(e) => Failure(new Error(s"[Error] Unable to create the XML Reader. ${e.getMessage}"))
        }
    }

  /**
    * Build the nu.xom.Document representing the xml
    */
  def build(xml: InputStream): Try[Document] =
    try {
      val builder = new Builder
      Success(builder.build(xml))
    } catch { 
       case e: ValidityException => Failure( new Error( msg(e) ) )
       case e: ParsingException  => Failure( new Error( msg(e) ) )
       case e: Exception => Failure( new Error(s"[Error] Unable to parse the XML document. ${e.getMessage}"))
    }

  private def loadSchema(xsdPath : String): Schema = {
    try {
      val schemaURL: URL = Thread.currentThread.getContextClassLoader.getResource(xsdPath)
      val schemaFactory: SchemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
      return schemaFactory.newSchema(schemaURL)
    }
    catch {
      case e: SAXException => {
        e.printStackTrace
        return null
      }
    }
  }

  private def createXMLReader( xsd: InputStream, resolver: Option[LSResourceResolver] = None ): Try[XMLReader] = 
    try {
      // Create the schema
      println("create the schema");
      val factory =  SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
      println("apply resolver")
      if(resolver!=null) {
        resolver match {
          case Some(r) => {
            println("resolver found, applying")
            if (r != Nil) {
              factory.setResourceResolver(r)
            }
          };
          case _ => println("no resolver")
        }
      }

      val xsdContent = IOUtils.toString(xsd)

      val stringReader: StringReader = new StringReader(xsdContent)
      var schema : Schema = null;
      try{
       schema = factory.newSchema(new StreamSource(stringReader))
      }catch {
        case e:Exception => {
          println("Unable to create a schema : "+e.getMessage)
          return Success(null)
        }
      }
      // Setup SAX parser for schema validation
      println("Setup SAX parser for schema validation");
      val spf = SAXParserFactory.newInstance()
      spf.setNamespaceAware(true)
      // For some reasons when this is set the code is failing with 'Document is invalid: no grammar found.'
      // It seems like it is expecting an internal DTD or schema
      // spf.setValidating(true) // only works for DTD
      spf.setSchema(schema)
      println("create parser and reader");
      val parser = spf.newSAXParser
      val reader = parser.getXMLReader
      // Setup the error handler
      println("Set error handler");
      reader.setErrorHandler( new CustomErrorHandler )
      Success( reader )
    } catch { case e: Exception => Failure( e ) }

  def msg(ve: ValidityException) = {
    val nbError = ve.getErrorCount
    val details = (0 until nbError) map { i =>
      s"${ve.getValidityError(i)} At line ${ve.getLineNumber(i)}, column ${ve.getColumnNumber(i)}" 
    } mkString("\t- ","\n\t- ", "")
    s"The document is invalid (validity). ${nbError} error(s) found.\n  ${ details }"
  }

  def msg(pe: ParsingException) =
    s"The document is invalid (parsing).\n\t[Error] ${pe.getMessage} At line ${pe.getLineNumber}, column ${pe.getColumnNumber} "

}

class CustomErrorHandler extends ErrorHandler { 

  def warning(e: SAXParseException) = throw e // fail fast

  def error(e: SAXParseException) = throw e // fail fast

  def fatalError(e: SAXParseException) = throw e // fail fast
}